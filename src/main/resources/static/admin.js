const logoutBtn = document.getElementById("logoutBtn");
const usernameDisplay = document.getElementById("usernameDisplay");
const adminContent=document.getElementById("admin-content")
const menuItems = document.querySelectorAll(".menu-item");

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
    const response = await fetch("/api/admin/users");
    const users = await response.json();

    adminContent.innerHTML = "";

    const h = document.createElement("div");
    h.classList.add("user-titles");

    h.appendChild(createHeader("ID"));
    h.appendChild(createHeader("Email"));
    h.appendChild(createHeader("First Name"));
    h.appendChild(createHeader("Last Name"));
    h.appendChild(createHeader("Phone"));
    h.appendChild(createHeader("Username"));

    adminContent.appendChild(h);

    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }
    users.forEach(user => {
        const r = document.createElement("div");
        r.classList.add("user-row");

        r.appendChild(createCell(user.id));
        r.appendChild(createCell(user.email));
        r.appendChild(createCell(user.firstName));
        r.appendChild(createCell(user.lastName));
        r.appendChild(createCell(user.phone));
        r.appendChild(createCell(user.username));

        adminContent.appendChild(r);
    });

    function createCell(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }
}

async function loadCars() {

    adminContent.innerHTML = "";

    const response = await fetch("/api/admin/cars");
    const cars = await response.json();

    console.log("CARS FROM DB:", cars);

    const h = document.createElement("div");
    h.classList.add("car-titles");

    h.appendChild(createHeader("ID"));
    h.appendChild(createHeader("Name"));
    h.appendChild(createHeader("Type"));
    h.appendChild(createHeader("Model"));
    h.appendChild(createHeader("Price"));
    h.appendChild(createHeader("Image"));
    h.appendChild(createHeader("Feature1"));
    h.appendChild(createHeader("Feature2"));
    h.appendChild(createHeader("Feature3"));

    adminContent.appendChild(h);

    cars.forEach(car => {
        console.log("IMAGE VALUE:", car.image);
        console.log("IMAGE RAW:", car.image);
        console.log("TYPE:", typeof car.image);

        const r = document.createElement("div");
        r.classList.add("car-row");

        r.appendChild(createCell(car.id));
        r.appendChild(createCell(car.name));
        r.appendChild(createCell(car.type));
        r.appendChild(createCell(car.model));
        r.appendChild(createCell(car.price));

        // visa bild
        const imgCell = document.createElement("div");
        const img = document.createElement("img");
        img.src = "data:image/jpeg;base64," + car.image;
        img.classList.add("car-image");
        imgCell.appendChild(img);
        r.appendChild(imgCell);

        r.appendChild(createCell(car.feature1));
        r.appendChild(createCell(car.feature2));
        r.appendChild(createCell(car.feature3));

        adminContent.appendChild(r);
    });

    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }

    function createCell(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }
}

async function loadBookings() {
    adminContent.innerHTML = "";

    const response = await fetch("/api/admin/bookings");
    const bookings = await response.json();
    const h = document.createElement("div");

    h.classList.add("booking-titles");

    h.appendChild(createHeader("ID"));
    h.appendChild(createHeader("Active"));
    h.appendChild(createHeader("Car"));
    h.appendChild(createHeader("User"));
    h.appendChild(createHeader("From"));
    h.appendChild(createHeader("To"));
    h.appendChild(createHeader("Price"));

    adminContent.appendChild(h);

    bookings.forEach(b => {
        const r = document.createElement("div");
        r.classList.add("booking-row");

        r.appendChild(createCell(b.id));
        r.appendChild(createCell(b.active));
        r.appendChild(createCell(b.car?.name ?? "-"));
        r.appendChild(createCell(b.user?.username ?? "-"));
        r.appendChild(createCell(b.fromDate ?? "-"));
        r.appendChild(createCell(b.toDate ?? "-"));
        r.appendChild(createCell("-"));

        adminContent.appendChild(r);
    });

    function createHeader(text) {
        const d = document.createElement("div");
        d.textContent = text;
        return d;
    }

    function createCell(text) {
        const d = document.createElement("div");
        d.textContent = text ?? "-";
        return d;
    }
}


