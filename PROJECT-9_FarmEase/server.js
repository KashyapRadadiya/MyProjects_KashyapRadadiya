require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const cors = require("cors");
const session = require("express-session");

const app = express();
app.use(express.json());
app.use(cors({
    origin: ["http://localhost:5500","http://127.0.0.1:5500"], // Update with your frontend URL
    credentials: true
})); 

// Session Middleware
app.use(session({
    secret: process.env.SESSION_SECRET || "mysecretkey",
    resave: false,
    saveUninitialized: false,
    cookie: { secure: false }
}));

// Connect to MongoDB mongodb://localhost:27017/FarmEase
mongoose.connect(process.env.MONGO_URI || "mongodb://127.0.0.1:27017/FarmEase")
    .then(() => console.log("✅ MongoDB Connected"))
    .catch(err => console.log("❌ MongoDB Error:", err));

// User Schema & Model
const UserSchema = new mongoose.Schema({
    name: String,
    email: { type: String, unique: true },
    password: String
},{ collection: "usercreds" });

const User = mongoose.model("User", UserSchema);

// Register User
app.post("/register", async (req, res) => {
    try {
        const { name, email, password } = req.body;
        const existingUser = await User.findOne({ email });

        if (existingUser) return res.status(400).json({ message: "Email already exists!" });

        const hashedPassword = await bcrypt.hash(password, 10);
        const user = new User({ name, email, password: hashedPassword });

        await user.save();
        res.json({ message: "User registered successfully!" });
    } catch (error) {
        console.error("❌ Registration Error:", error);
        res.status(500).json({ message: "Server error!", error });
    }
});

// Login User
app.post("/login", async (req, res) => {
    try {
        const { email, password } = req.body;
        const user = await User.findOne({ email });

        if (!user) return res.status(400).json({ message: "User not found!" });

        const isValid = await bcrypt.compare(password, user.password);
        if (!isValid) return res.status(400).json({ message: "Incorrect password!" });

        // Generate JWT Token
        const token = jwt.sign({ id: user._id, name: user.name, email: user.email }, 
            process.env.JWT_SECRET || "secretkey", { expiresIn: "1h" });

        // Store User Session
        req.session.user = { id: user._id, name: user.name, email: user.email };

        res.json({ token, user: req.session.user });
    } catch (error) {
        res.status(500).json({ message: "Server error!", error });
    }
});

// Get User Profile
app.get("/profile", (req, res) => {
    if (!req.session.user) return res.status(401).json({ message: "Unauthorized" });
    res.json(req.session.user);
});

// Check Session (For Navbar Update)
app.get("/session", (req, res) => {
    res.json({ user: req.session.user || null });
});

// Logout User
app.post("/logout", (req, res) => {
    req.session.destroy();
    res.json({ message: "Logged out successfully!" });
});

// Start Server
const PORT = process.env.PORT || 3040;
app.listen(PORT, () => {
    console.log(`🚀 Server running on port ${PORT}`);
});
