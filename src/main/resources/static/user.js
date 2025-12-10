const logoutBtn = document.getElementById("logoutBtn");
const usernameDisplay = document.getElementById("usernameDisplay");
const userContent=document.getElementById("userContent")
const menuItems = document.querySelectorAll(".menu-item");
let currentSort = "name-asc";

if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        window.location.href = "/logout";
    });
}

menuItems.forEach(item => {
    item.addEventListener("click", () => {
        const view = item.getAttribute("data-view");
        switch (view) {
            case "cars":
                loadCars();
                break;
            case "bookings":
                loadBookings();
                break
        }
    })
})

function normalizeArray(obj) {
    if (!obj) return [];
    if (Array.isArray(obj)) return obj;
    if (Array.isArray(obj.content)) return obj.content;
    if (Array.isArray(obj.data)) return obj.data;
    if (Array.isArray(obj.cars)) return obj.cars;
    // try to find first array property
    for (const k of Object.keys(obj)) {
        if (Array.isArray(obj[k])) return obj[k];
    }
    return [];
}


async function loadCars() {

    userContent.innerHTML = "";

    const sortingBar = document.createElement("div");
    sortingBar.classList.add("sorting-bar");
    sortingBar.innerHTML = `
        <label>Sortera:</label>
        <select id="sortSelect">
            <option value="name-asc">Namn A–Ö</option>
            <option value="name-desc">Namn Ö–A</option>
            <option value="type-asc">Typ A–Ö</option>
            <option value="type-desc">Typ Ö–A</option>
        </select>
    `;
    userContent.appendChild(sortingBar);

    // نحصل على الريفرنس بعد ما نضيف الـ HTML
    const sortSelect = document.getElementById("sortSelect");
    // نخلي القيمة اللي اختارها المستخدم آخر مرة
    sortSelect.value = currentSort;

    // لما يغيّر السورت
    sortSelect.onchange = () => {
        currentSort = sortSelect.value; // خزّن الاختيار
        loadCars();                     // أعد تحميل القائمة
    };

    // جلب السيارات
    const response = await fetch("/api/user/cars", { credentials: "include" });
    let cars = await response.json();

    // طبق السورت حسب currentSort
    cars = sortCars(cars, currentSort);

    // الهيدر
    const header = document.createElement("div");
    header.classList.add("car-titles");

    ["ID","Name","Type","Model","Price","Image","Feature1","Feature2","Feature3"].forEach(t => {
        const d = document.createElement("div");
        d.textContent = t;
        header.appendChild(d);
    });
    userContent.appendChild(header);

    // لو ما في سيارات
    if (!cars || cars.length === 0) {
        const empty = document.createElement("div");
        empty.className = "empty";
        empty.textContent = "Inga bilar hittades";
        userContent.appendChild(empty);
        return;
    }

    // عرض السيارات
    cars.forEach(car => {
        const row = document.createElement("div");
        row.classList.add("car-row");

        [car.id, car.name, car.type, car.model, car.price].forEach(v => {
            const d = document.createElement("div");
            d.textContent = v ?? "-";
            row.appendChild(d);
        });

        const imgCell = document.createElement("div");
        const img = document.createElement("img");
        if (car.image) {
            img.src = "data:image/jpeg;base64," + car.image;
            img.classList.add("car-image");
            img.onerror = () => img.style.display = "none";
        }
        imgCell.appendChild(img);
        row.appendChild(imgCell);

        [car.feature1, car.feature2, car.feature3].forEach(v => {
            const d = document.createElement("div");
            d.textContent = v ?? "-";
            row.appendChild(d);
        });

        userContent.appendChild(row);
    });
}


function sortCars(cars, rule) {
    switch (rule) {
        case "name-asc":
            return cars.sort((a, b) => a.name.localeCompare(b.name, "sv"));
        case "name-desc":
            return cars.sort((a, b) => b.name.localeCompare(a.name, "sv"));
        case "type-asc":
            return cars.sort((a, b) => a.type.localeCompare(b.type, "sv"));
        case "type-desc":
            return cars.sort((a, b) => b.type.localeCompare(a.type, "sv"));
        default:
            return cars;
    }
}


async function loadBookings() {
    if (!userContent) return;
    userContent.innerHTML = "";

    try {
        const response = await fetch("/api/user/my-bookings", { credentials: "include" });
        if (!response.ok) {
            const body = await response.text();
            if (response.status === 401 || response.status === 403) {
                return;
            }
            console.error("Failed to fetch bookings:", response.status, body);
            userContent.innerHTML = `<div class="empty">Error ${response.status}: ${body || 'Failed to load bookings'}</div>`;
            return;
        }
        const json = await response.json();
        const bookings = normalizeArray(json);

        const h = document.createElement("div");
        h.classList.add("booking-titles");
        ["ID","Active","Car","User","From","To","Price"].forEach(t => {
            const d = document.createElement("div"); d.textContent = t; h.appendChild(d);
        });
        userContent.appendChild(h);

        if (bookings.length === 0) {
            userContent.appendChild(Object.assign(document.createElement("div"), { className: "empty", textContent: "No bookings found" }));
            return;
        }

        bookings.forEach(b => {
            const r = document.createElement("div");
            r.classList.add("booking-row");
            const cells = [
                b.id,
                b.active,
                b.car?.name ?? "-",
                b.user?.username ?? "-",
                b.fromDate ?? "-",
                b.toDate ?? "-",
                b.price ?? "-"
            ];
            cells.forEach(v => { const d = document.createElement("div"); d.textContent = v; r.appendChild(d); });
            userContent.appendChild(r);
        });
    } catch (err) {
        console.error("Error loading bookings:", err);
        userContent.innerHTML = `<div class="empty">Network error loading bookings</div>`;
    }
}