/* SmartStock frontend - plain JavaScript, talks to the Spring Boot REST API (/api/...) */

const $ = (sel, el = document) => el.querySelector(sel);
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const money = n => '₹' + Number(n || 0).toLocaleString('en-IN', { maximumFractionDigits: 2 });
const fmtDate = d => new Date(d).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
const debounce = (fn, ms) => { let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), ms); }; };

const cache = { products: [], categories: [], suppliers: [] };
let current = 'dashboard';

async function api(path, method = 'GET', body) {
  const res = await fetch('/api' + path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined
  });
  if (!res.ok) {
    let msg = 'Something went wrong. Please try again.';
    try { msg = (await res.json()).message || msg; } catch (e) { /* ignore */ }
    throw new Error(msg);
  }
  return res.status === 204 ? null : res.json();
}

function toast(msg, error = false) {
  const t = $('#toast');
  t.textContent = msg;
  t.className = 'show' + (error ? ' error' : '');
  clearTimeout(toast.timer);
  toast.timer = setTimeout(() => { t.className = ''; }, 3200);
}

async function run(fn) {
  try { await fn(); } catch (e) { toast(e.message, true); }
}

/* ---------- small HTML helpers (cells are HTML strings: escape user data with esc()) ---------- */
function table(headers, rows, empty) {
  if (!rows.length) return `<div class="empty">${empty}</div>`;
  const head = headers.map(h => `<th class="${h.num ? 'num' : ''}">${h.label ?? h}</th>`).join('');
  const body = rows.map(r => `<tr>${r.map((c, i) => `<td class="${headers[i].num ? 'num' : ''} ${headers[i].actions ? 'row-actions' : ''}">${c}</td>`).join('')}</tr>`).join('');
  return `<div class="table-wrap"><table><thead><tr>${head}</tr></thead><tbody>${body}</tbody></table></div>`;
}
const btn = (act, id, label, cls = 'ghost') => `<button class="btn small ${cls}" data-act="${act}" data-id="${id}">${label}</button>`;
const stat = (label, value) => `<div class="stat"><span>${label}</span><strong>${value}</strong></div>`;
const typeBadge = t => `<span class="badge ${t === 'IN' ? 'in' : 'out'}">${t === 'IN' ? 'Stock in' : 'Stock out'}</span>`;

function movementsTable(list) {
  return table(
    ['Date', 'Product', 'Type', { label: 'Quantity', num: true }, 'Note'],
    list.map(t => [fmtDate(t.createdAt), esc(t.product.name), typeBadge(t.type), t.quantity, esc(t.note)]),
    'No stock movements yet. Record your first stock in or stock out from the Products page.'
  );
}

/* ---------- generic modal form ---------- */
function openForm(title, fields, values, onSubmit) {
  $('#modalTitle').textContent = title;
  $('#modalFields').innerHTML = fields.map(f => {
    const v = values?.[f.name] ?? f.default ?? '';
    const req = f.required ? 'required' : '';
    let input;
    if (f.type === 'select') {
      const opts = f.options.map(o => `<option value="${esc(o.value)}" ${String(o.value) === String(v) ? 'selected' : ''}>${esc(o.label)}</option>`).join('');
      input = `<select name="${f.name}" ${req}>${f.optional ? '<option value="">None</option>' : ''}${opts}</select>`;
    } else if (f.type === 'textarea') {
      input = `<textarea name="${f.name}">${esc(v)}</textarea>`;
    } else {
      input = `<input name="${f.name}" type="${f.type || 'text'}" value="${esc(v)}" ${req} ${f.min !== undefined ? `min="${f.min}"` : ''} ${f.step ? `step="${f.step}"` : ''}>`;
    }
    return `<label>${esc(f.label)}${input}</label>`;
  }).join('');

  const form = $('#modalForm');
  form.onsubmit = async e => {
    e.preventDefault();
    try {
      await onSubmit(Object.fromEntries(new FormData(form)));
      $('#modal').close();
    } catch (err) {
      toast(err.message, true);
    }
  };
  $('#modal').showModal();
}
$('#modalCancel').onclick = () => $('#modal').close();

/* ---------- views ---------- */
const views = {
  dashboard: { title: 'Dashboard', render: renderDashboard },
  products: { title: 'Products', render: renderProducts },
  categories: { title: 'Categories', render: renderCategories },
  suppliers: { title: 'Suppliers', render: renderSuppliers },
  stock: { title: 'Stock movements', render: renderStock }
};

function show(name) {
  current = name;
  document.querySelectorAll('nav button').forEach(b => b.classList.toggle('active', b.dataset.view === name));
  $('#title').textContent = views[name].title;
  $('#actions').innerHTML = '';
  run(views[name].render);
}
const refresh = () => show(current);

/* Dashboard */
async function renderDashboard() {
  const d = await api('/dashboard');
  const low = d.lowStock;
  const alertBlock = low.length
    ? `<div class="alerts">
         <h2>${low.length} ${low.length === 1 ? 'product needs' : 'products need'} restocking</h2>
         <p>These items are at or below their reorder level.</p>
         ${table(
           ['SKU', 'Product', 'Supplier', { label: 'In stock', num: true }, { label: 'Reorder level', num: true }],
           low.map(p => [esc(p.sku), esc(p.name), esc(p.supplier?.name || '-'), `<span class="badge low">${p.quantity}</span>`, p.reorderLevel]),
           ''
         )}
       </div>`
    : `<div class="alerts ok"><h2>Everything is stocked</h2><p>No product is below its reorder level.</p></div>`;

  $('#content').innerHTML = alertBlock +
    `<div class="stats">
       ${stat('Products', d.totalProducts)}
       ${stat('Units in stock', d.totalUnits.toLocaleString('en-IN'))}
       ${stat('Stock value', money(d.totalValue))}
       ${stat('Categories', d.categories)}
       ${stat('Suppliers', d.suppliers)}
     </div>
     <h2 class="sub">Recent stock movements</h2>` + movementsTable(d.recent);
}

/* Products */
async function renderProducts() {
  $('#actions').innerHTML = `<input id="search" class="search" type="search" placeholder="Search by name or SKU"><button class="btn primary" id="addBtn">Add product</button>`;
  $('#addBtn').onclick = () => run(() => productForm());
  $('#search').oninput = debounce(() => run(loadProducts), 250);
  await loadProducts();
}

async function loadProducts() {
  const q = $('#search')?.value.trim() || '';
  const list = await api('/products' + (q ? '?search=' + encodeURIComponent(q) : ''));
  cache.products = list;
  $('#content').innerHTML = table(
    ['SKU', 'Product', 'Category', 'Supplier', { label: 'Price', num: true }, { label: 'In stock', num: true }, { label: 'Reorder level', num: true }, { label: '', actions: true }],
    list.map(p => [
      esc(p.sku), esc(p.name), esc(p.category?.name || '-'), esc(p.supplier?.name || '-'),
      money(p.price),
      `<span class="badge ${p.quantity <= p.reorderLevel ? 'low' : 'ok'}">${p.quantity}</span>`,
      p.reorderLevel,
      btn('stock-product', p.id, 'Stock in/out') + btn('edit-product', p.id, 'Edit') + btn('del-product', p.id, 'Delete', 'danger')
    ]),
    q ? 'No product matches your search.' : 'No products yet. Select Add product to create the first one.'
  );
}

async function productForm(p) {
  const [cats, sups] = await Promise.all([api('/categories'), api('/suppliers')]);
  openForm(p ? 'Edit product' : 'Add product', [
    { name: 'sku', label: 'SKU', required: true },
    { name: 'name', label: 'Product name', required: true },
    { name: 'description', label: 'Description', type: 'textarea' },
    { name: 'price', label: 'Price (₹)', type: 'number', required: true, min: 0, step: '0.01' },
    { name: 'quantity', label: 'Quantity in stock', type: 'number', required: true, min: 0, default: 0 },
    { name: 'reorderLevel', label: 'Reorder level', type: 'number', required: true, min: 0, default: 10 },
    { name: 'categoryId', label: 'Category', type: 'select', optional: true, options: cats.map(c => ({ value: c.id, label: c.name })) },
    { name: 'supplierId', label: 'Supplier', type: 'select', optional: true, options: sups.map(s => ({ value: s.id, label: s.name })) }
  ], p ? { ...p, categoryId: p.category?.id, supplierId: p.supplier?.id } : null, async d => {
    await api(p ? '/products/' + p.id : '/products', p ? 'PUT' : 'POST', {
      sku: d.sku.trim(),
      name: d.name.trim(),
      description: d.description,
      price: Number(d.price),
      quantity: parseInt(d.quantity, 10),
      reorderLevel: parseInt(d.reorderLevel, 10),
      categoryId: d.categoryId ? Number(d.categoryId) : null,
      supplierId: d.supplierId ? Number(d.supplierId) : null
    });
    toast('Product saved');
    refresh();
  });
}

/* Stock in / out (product optional: when null the form shows a product picker) */
async function stockForm(p) {
  const fields = [];
  if (!p) {
    const list = await api('/products');
    fields.push({ name: 'productId', label: 'Product', type: 'select', required: true, options: list.map(x => ({ value: x.id, label: `${x.name} (${x.sku}) - ${x.quantity} in stock` })) });
  }
  fields.push(
    { name: 'type', label: 'Movement', type: 'select', required: true, options: [{ value: 'IN', label: 'Stock in (received)' }, { value: 'OUT', label: 'Stock out (sold or used)' }] },
    { name: 'quantity', label: 'Quantity', type: 'number', required: true, min: 1, default: 1 },
    { name: 'note', label: 'Note (optional)' }
  );
  openForm(p ? `Stock movement: ${p.name}` : 'Record stock movement', fields, null, async d => {
    await api('/stock', 'POST', {
      productId: p ? p.id : Number(d.productId),
      type: d.type,
      quantity: parseInt(d.quantity, 10),
      note: d.note
    });
    toast('Stock updated');
    refresh();
  });
}

/* Categories */
async function renderCategories() {
  $('#actions').innerHTML = `<button class="btn primary" id="addBtn">Add category</button>`;
  $('#addBtn').onclick = () => categoryForm();
  const list = await api('/categories');
  cache.categories = list;
  $('#content').innerHTML = table(
    ['Name', 'Description', { label: '', actions: true }],
    list.map(c => [esc(c.name), esc(c.description), btn('edit-category', c.id, 'Edit') + btn('del-category', c.id, 'Delete', 'danger')]),
    'No categories yet. Select Add category to create one.'
  );
}

function categoryForm(c) {
  openForm(c ? 'Edit category' : 'Add category', [
    { name: 'name', label: 'Name', required: true },
    { name: 'description', label: 'Description', type: 'textarea' }
  ], c, async d => {
    await api(c ? '/categories/' + c.id : '/categories', c ? 'PUT' : 'POST', { name: d.name, description: d.description });
    toast('Category saved');
    refresh();
  });
}

/* Suppliers */
async function renderSuppliers() {
  $('#actions').innerHTML = `<button class="btn primary" id="addBtn">Add supplier</button>`;
  $('#addBtn').onclick = () => supplierForm();
  const list = await api('/suppliers');
  cache.suppliers = list;
  $('#content').innerHTML = table(
    ['Name', 'Email', 'Phone', 'Address', { label: '', actions: true }],
    list.map(s => [esc(s.name), esc(s.email), esc(s.phone), esc(s.address), btn('edit-supplier', s.id, 'Edit') + btn('del-supplier', s.id, 'Delete', 'danger')]),
    'No suppliers yet. Select Add supplier to create one.'
  );
}

function supplierForm(s) {
  openForm(s ? 'Edit supplier' : 'Add supplier', [
    { name: 'name', label: 'Name', required: true },
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'phone', label: 'Phone' },
    { name: 'address', label: 'Address', type: 'textarea' }
  ], s, async d => {
    await api(s ? '/suppliers/' + s.id : '/suppliers', s ? 'PUT' : 'POST', { name: d.name, email: d.email, phone: d.phone, address: d.address });
    toast('Supplier saved');
    refresh();
  });
}

/* Stock movements */
async function renderStock() {
  $('#actions').innerHTML = `<button class="btn primary" id="addBtn">Record movement</button>`;
  $('#addBtn').onclick = () => run(() => stockForm());
  $('#content').innerHTML = movementsTable(await api('/stock'));
}

/* ---------- row actions (event delegation) ---------- */
async function remove(path, question) {
  if (!confirm(question)) return;
  await api(path, 'DELETE');
  toast('Deleted');
  refresh();
}

const handlers = {
  'edit-product': id => productForm(cache.products.find(p => p.id == id)),
  'stock-product': id => stockForm(cache.products.find(p => p.id == id)),
  'del-product': id => remove('/products/' + id, 'Delete this product and its stock history?'),
  'edit-category': id => categoryForm(cache.categories.find(c => c.id == id)),
  'del-category': id => remove('/categories/' + id, 'Delete this category?'),
  'edit-supplier': id => supplierForm(cache.suppliers.find(s => s.id == id)),
  'del-supplier': id => remove('/suppliers/' + id, 'Delete this supplier?')
};

$('#content').addEventListener('click', e => {
  const b = e.target.closest('[data-act]');
  if (b) run(() => handlers[b.dataset.act](b.dataset.id));
});

document.querySelectorAll('nav button').forEach(b => { b.onclick = () => show(b.dataset.view); });
show('dashboard');
