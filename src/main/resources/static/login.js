alert("login.js loaded!"); // If you don't see this, your browser is using an old file.

function getCredentials() {
    return {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };
}

async function sendLoginRequest(credentials) {
    const params = new URLSearchParams();
    params.append("username", credentials.username);
    params.append("password", credentials.password);

    const response = await fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()
    });

    if (!response.ok) return null;
    return await response.json();
}


function redirectUser(user) {
    if (user.role === "ADMIN") {
        window.location.href = "/admin.html";
    } else {
        window.location.href = "/main.html";
    }
}

function showError(msg) {
    document.getElementById("loginError").textContent = msg;
}


document.getElementById("loginBtn").addEventListener("click", async () => {

    const credentials = getCredentials();
    const user = await sendLoginRequest(credentials);

    if (!user) {
        showError("Fel användarnamn eller lösenord");
        return;
    }

    redirectUser(user);
});