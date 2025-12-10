const logoutBtn = document.getElementById("logoutBtn");
const usernameDisplay = document.getElementById("usernameDisplay");
const adminContent=document.getElementById("adminContent")
const menuItems = document.querySelectorAll(".menu-item");
let currentSort = "id-asc";

let adminSort = {
    column: null,
    direction: "asc"
};

logoutBtn.addEventListener("click", () => {
    window.location.href = "/logout";
});

menuItems.forEach(item => {
    item.addEventListener("click", () => {
        const view = item.getAttribute("data-view");
        switch (view) {
            case "users":
                loadUsers();
                break;
            case "cars":
                loadCars();
                break
            case "bookings":
                loadBookings();
                break;
        }
    })
})


async function loadUsers() {

    if (!adminContent) {
        console.error("adminContent not found (loadUsers)");
        return;
    }

    adminContent.innerHTML = "";

    const sortingBar = document.createElement("div");
    sortingBar.classList.add("sorting-bar");

    sortingBar.innerHTML = `
    <label>Sortera:</label>
    <select id="sortSelect">
        <option value="id-asc">ID 1–9</option>
        <option value="id-desc">ID 9–1</option>
        
        <option value="email-asc">Email A–Ö</option>
        <option value="email-desc">Email Ö–A</option>
        
        <option value="first-name-asc">Förnamn A–Ö</option>
        <option value="first-name-desc">Förnamn Ö–A</option>
        
        <option value="last-name-asc">Efternamn A–Ö</option>
        <option value="last-name-desc">Efternamn Ö–A</option>
        
        <option value="phone-asc">Telefonnummer 0–9</option>
        <option value="phone-desc">Telefonnummer 9–0</option>
        
        <option value="username-asc">Användarnamn A–Ö</option>
        <option value="username-desc">Användarnamn Ö–A</option>
    </select>
`;

    adminContent.appendChild(sortingBar);

    // نحصل على الريفرنس بعد ما نضيف الـ HTML
    const sortSelect = document.getElementById("sortSelect");
    // نخلي القيمة اللي اختارها المستخدم آخر مرة
    sortSelect.value = currentSort;

    // لما يغيّر السورت
    sortSelect.onchange = () => {
        currentSort = sortSelect.value; // خزّن الاختيار
        loadUsers();                     // أعد تحميل القائمة
    };


    try {
        const response = await fetch("/api/admin/users", {
            credentials: "include"   // comment: send session cookie
        });

        if (!response.ok) {
            console.error("Failed to load users:", response.status);
            adminContent.textContent = "Kunde inte hämta användare.";
            return;
        }

        const json = await response.json();
        const users = Array.isArray(json) ? json : json.data || [];

        switch (currentSort) {
            case "id-asc":
                users.sort((a, b) => a.id - b.id);
                break;
            case "id-desc":
                users.sort((a, b) => b.id - a.id);
                break;

            case "email-asc":
                users.sort((a, b) => a.email.localeCompare(b.email, "sv"));
                break;
            case "email-desc":
                users.sort((a, b) => b.email.localeCompare(a.email, "sv"));
                break;

            case "first-name-asc":
                users.sort((a, b) => a.firstName.localeCompare(b.firstName, "sv"));
                break;
            case "first-name-desc":
                users.sort((a, b) => b.firstName.localeCompare(a.firstName, "sv"));
                break;

            case "last-name-asc":
                users.sort((a, b) => a.lastName.localeCompare(b.lastName, "sv"));
                break;
            case "last-name-desc":
                users.sort((a, b) => b.lastName.localeCompare(a.lastName, "sv"));
                break;

            case "phone-asc":
                users.sort((a, b) => (a.phone || "").localeCompare(b.phone || "", "sv"));
                break;
            case "phone-desc":
                users.sort((a, b) => (b.phone || "").localeCompare(a.phone || "", "sv"));
                break;

            case "username-asc":
                users.sort((a, b) => a.username.localeCompare(b.username, "sv"));
                break;
            case "username-desc":
                users.sort((a, b) => b.username.localeCompare(a.username, "sv"));
                break;
        }

        console.log("USERS FROM DB:", users);

        // ----- titles -----
        const h = document.createElement("div");
        h.classList.add("user-titles");

        ["ID", "Email", "First Name", "Last Name", "Phone", "Username"]
            .forEach(t => h.appendChild(createHeader(t)));

        adminContent.appendChild(h);

        // ----- rows -----
        users.forEach(u => {
            const r = document.createElement("div");
            r.classList.add("user-row");

            r.appendChild(createCell(u.id));
            r.appendChild(createCell(u.email));
            r.appendChild(createCell(u.firstName));
            r.appendChild(createCell(u.lastName));
            r.appendChild(createCell(u.phone));
            r.appendChild(createCell(u.username));

            adminContent.appendChild(r);
        });

    } catch (err) {
        console.error("Error loading users:", err);
        adminContent.textContent = "Fel inträffade vid hämtning av användare.";
    }

    // ----- helpers -----
    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }

    function createCell(value) {
        const d = document.createElement("div");
        d.textContent = value ?? "-";
        return d;
    }
}


async function loadCars() {

    // comment: safety check
    if (!adminContent) {
        console.error("adminContent not found (loadCars)");
        return;
    }

    adminContent.innerHTML = "";

    try {
        const response = await fetch("/api/admin/cars", {
            credentials: "include"   // comment: send session cookie
        });

        if (!response.ok) {
            console.error("Failed to load cars (status):", response.status);
            adminContent.textContent = "Kunde inte hämta bilar.";
            return;
        }

        const json = await response.json();
        const cars = Array.isArray(json) ? json : json.data || [];

        console.log("CARS FROM DB:", cars);

        // ----- headers -----
        const h = document.createElement("div");
        h.classList.add("car-titles");

        ["ID", "Name", "Type", "Model", "Price", "Image", "Feature1", "Feature2", "Feature3"]
            .forEach(t => h.appendChild(createHeader(t)));

        adminContent.appendChild(h);

        // ----- rows -----
        cars.forEach(car => {
            const r = document.createElement("div");
            r.classList.add("car-row");

            r.appendChild(createCell(car.id));
            r.appendChild(createCell(car.name));
            r.appendChild(createCell(car.type));
            r.appendChild(createCell(car.model));
            r.appendChild(createCell(car.price));

            // ----- image -----
            const imgCell = document.createElement("div");
            const img = document.createElement("img");

            if (car.image) {
                img.src = "data:image/jpeg;base64," + car.image;
            }

            img.classList.add("car-image");
            imgCell.appendChild(img);
            r.appendChild(imgCell);

            r.appendChild(createCell(car.feature1));
            r.appendChild(createCell(car.feature2));
            r.appendChild(createCell(car.feature3));

            adminContent.appendChild(r);
        });

    } catch (err) {
        console.error("Error loading cars:", err);
        adminContent.textContent = "Fel inträffade vid hämtning av bilar.";
    }


    // --------- helpers ---------
    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }

    function createCell(value) {
        const d = document.createElement("div");
        d.textContent = value ?? "-";
        return d;
    }
}



async function loadBookings() {

    // comment: safety check
    if (!adminContent) {
        console.error("adminContent not found (loadBookings)");
        return;
    }

    adminContent.innerHTML = "";

    try {
        const response = await fetch("/api/admin/bookings", {
            credentials: "include"   // comment: send session cookie
        });

        if (!response.ok) {
            console.error("Failed to load bookings (status):", response.status);
            adminContent.textContent = "Kunde inte hämta bokningar.";
            return;
        }

        const json = await response.json();
        const bookings = Array.isArray(json) ? json : json.data || [];

        console.log("BOOKINGS FROM DB:", bookings);

        // ----- headers -----
        const h = document.createElement("div");
        h.classList.add("booking-titles");

        ["ID", "Active", "Car", "User", "From", "To", "Price"]
            .forEach(t => h.appendChild(createHeader(t)));

        adminContent.appendChild(h);

        // ----- rows -----
        bookings.forEach(b => {
            const r = document.createElement("div");
            r.classList.add("booking-row");

            r.appendChild(createCell(b.id));
            r.appendChild(createCell(b.active));
            r.appendChild(createCell(b.car?.name ?? "-"));
            r.appendChild(createCell(b.user?.username ?? "-"));
            r.appendChild(createCell(b.fromDate ?? "-"));
            r.appendChild(createCell(b.toDate ?? "-"));
            r.appendChild(createCell(b.price ?? "-"));

            adminContent.appendChild(r);
        });

    } catch (err) {
        console.error("Error loading bookings:", err);
        adminContent.textContent = "Fel inträffade vid hämtning av bokningar.";
    }

    // ----- helpers -----
    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }

    function createCell(value) {
        const d = document.createElement("div");
        d.textContent = value ?? "-";
        return d;
    }
}

