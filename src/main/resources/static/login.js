alert("login.js is loaded");

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

    if (!user || !user.role) {
        window.location.href = "/400.html";
        return;
    }

    switch (user.role.toUpperCase()) {
        case "ADMIN":
            window.location.href = "/admin.html";
            break;

        case "USER":
            window.location.href = "/user.html";
            break;

        default:
            window.location.href = "/400.html";
    }
}


function showError(msg) {
    document.getElementById("loginError").textContent = msg;
}

document.getElementById("loginBtn").onclick = handleLogin;


async function handleLogin() {

    const creds = getCredentials();
    const user = await sendLoginRequest(creds);

    if (!user) {
        showError("Fel användarnamn eller lösenord");
        return;
    }

    console.log("FULL USER:", user);

    redirectUser(user);
}