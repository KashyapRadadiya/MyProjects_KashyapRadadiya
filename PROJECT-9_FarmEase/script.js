document.addEventListener("DOMContentLoaded", async () => {
    const authLinks = document.getElementById("auth-links");
    const userInfo = document.getElementById("user-info");
    const usernameSpan = document.getElementById("username");
    const logoutBtn = document.getElementById("logout");

    // ✅ Check session and update navbar
    try {
        const response = await fetch("http://localhost:3040/session", { credentials: "include" });
        const data = await response.json();

        if (data.user) {
            authLinks.style.display = "none";
            userInfo.style.display = "inline-block";
            usernameSpan.textContent = data.user.name;
        } else {
            authLinks.style.display = "inline-block";
            userInfo.style.display = "none";
        }
    } catch (error) {
        console.error("Error fetching session:", error);
    }

    // ✅ Fix Login Form Submission Issue
    const loginForm = document.getElementById("login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault(); // ⛔ Prevent default page reload

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            console.log("Logging in with:", email, password); // ✅ Debugging

            try {
                const response = await fetch("http://localhost:3040/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password }),
                    credentials: "include"
                });

                const data = await response.json();
                console.log("Login response:", data); // ✅ Debugging

                if (response.ok) {
                    localStorage.setItem("token", data.token);
                    window.location.href = "index.html"; // ✅ Redirect to home page
                } else {
                    alert(data.message || "Login failed!");
                }
            } catch (error) {
                console.error("Error during login:", error);
                alert("Something went wrong. Please try again.");
            }
        });
    }

    // ✅ Fix Register Form Submission Issue
    const registerForm = document.getElementById("register-form");
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault(); // ⛔ Prevent default page reload

            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            console.log("Registering with:", name, email, password); // ✅ Debugging

            try {
                const response = await fetch("http://localhost:3040/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ name, email, password })
                });

                const data = await response.json();
                console.log("Register response:", data); // ✅ Debugging

                if (response.ok) {
                    window.location.href = "login.html"; // ✅ Redirect to login page
                } else {
                    alert(data.message || "Registration failed!");
                }
            } catch (error) {
                console.error("Error during registration:", error);
                alert("Something went wrong. Please try again.");
            }
        });
    }

    // ✅ Logout Functionality
    logoutBtn?.addEventListener("click", async () => {
        try {
            await fetch("http://localhost:3040/logout", { method: "POST", credentials: "include" });
            localStorage.removeItem("token");
            window.location.href = "index.html"; // ✅ Redirect to home after logout
        } catch (error) {
            console.error("Error during logout:", error);
        }
    });
});
