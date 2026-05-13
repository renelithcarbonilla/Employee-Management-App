const express = require("express"); // Fix: Refined SQL join for name matching
const mysql = require("mysql");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const db = mysql.createConnection({
  host: "127.0.0.1", 
  user: "root",
  password: "",
  database: "payroll" 
});

db.connect(err => {
  if (err) console.log("❌ DB Error:", err);
  else console.log("✅ Node.js Server is Connected!");
});

// --- LOGIN, PROFILE, & PAYSLIP ROUTES  ---

app.post("/login", (req, res) => {
    const { username, password } = req.body;
    const sql = "SELECT id, fullname FROM user WHERE username=? AND password=?";
    db.query(sql, [username, password], (err, result) => {
        if (err) return res.status(500).json({ status: "error", message: err.message });
        if (result.length > 0) {
            console.log(`🚀 [LOGIN] Success: ${result[0].fullname}`);
            res.json({ status: "success", user_id: result[0].id, fullname: result[0].fullname });
        } else {
            res.json({ status: "error", message: "Invalid credentials" });
        }
    });
});

// 3. GET PROFILE API
app.get("/get_profile.php", (req, res) => {
  const userId = req.query.user_id;

  // 1. Kuhaon una nato ang fullname sa user base sa user_id
  const userSql = "SELECT fullname FROM user WHERE id = ?";
  
  db.query(userSql, [userId], (err, userResult) => {
    if (err || userResult.length === 0) return res.status(404).json({status: "error", message: "User not found"});

    const userFullname = userResult[0].fullname;

    // 2. I-match ang fullname sa user ngadto sa employee table
    // Gigamit nato ang eksakto nga match sa lname o fname+lname combination
    const empSql = `
      SELECT emp_type, division 
      FROM employee 
      WHERE lname = ? OR CONCAT(fname, ' ', lname) = ?`;

    db.query(empSql, [userFullname, userFullname], (err, empResult) => {
      if (err || empResult.length === 0) {
        return res.json({status: "error", message: "No employee record found for " + userFullname});
      }

      console.log(`👤 [PROFILE] Displaying data for: ${userFullname}`);
      res.json({
        status: "success",
        fullname: userFullname,
        emp_type: empResult[0].emp_type,
        division: empResult[0].division
      });
    });
  });
});

// 4. GET PAYSLIP API
app.get("/get_payslip.php", (req, res) => {
  const userId = req.query.user_id;

  const userSql = "SELECT fullname FROM user WHERE id = ?";
  
  db.query(userSql, [userId], (err, userResult) => {
    if (err || userResult.length === 0) return res.status(404).json({status: "error", message: "User not found"});

    const userFullname = userResult[0].fullname;

    const empSql = `
      SELECT net_pay, bonus, deduction, overtime 
      FROM employee 
      WHERE lname = ? OR CONCAT(fname, ' ', lname) = ?`;

    db.query(empSql, [userFullname, userFullname], (err, empResult) => {
      if (err || empResult.length === 0) {
        return res.json({status: "error", message: "No payslip found for " + userFullname});
      }

      console.log(`💰 [PAYSLIP] Displaying data for: ${userFullname}`);
      res.json({
        status: "success",
        fullname: userFullname,
        net_pay: parseFloat(empResult[0].net_pay) || 0.0,
        bonus: parseFloat(empResult[0].bonus) || 0.0,
        deduction: parseFloat(empResult[0].deduction) || 0.0,
        overtime: parseFloat(empResult[0].overtime) || 0.0
      });
    });
  });
});

// --- ATTENDANCE API (FIXED FOR image_9ebfc1.png) ---

app.post("/api_attendance.php", (req, res) => {
    const userId = req.body.user_id;
    const action = req.body.action; 
    
    const now = new Date();
    const date = now.toISOString().slice(0, 10); 
    const time = now.toLocaleTimeString('en-GB'); 

    console.log(`-----------------------------------------`);
    console.log(`⏰ [ATTENDANCE] Request: User ${userId} wants to ${action}`);

    if (action === "Time In") {
        const sql = "INSERT INTO attendance (user_id, attendance_date, time_in, status) VALUES (?, ?, ?, 'Present')";
        db.query(sql, [userId, date, time], (err, result) => {
            if (err) return res.status(500).json({ status: "error", message: err.message });
            console.log(`✅ [TIME IN SUCCESS] recorded for User ${userId}`);
            res.json({ status: "success", message: "Time In recorded successfully!" });
        });
    } else if (action === "Time Out") {
        const sql = "UPDATE attendance SET time_out = ? WHERE user_id = ? AND attendance_date = ? AND time_out IS NULL";
        db.query(sql, [time, userId, date], (err, result) => {
            if (err) return res.status(500).json({ status: "error", message: err.message });
            
            if (result.affectedRows === 0) {
                console.log(`⚠️ [TIME OUT] No active Time In record for today.`);
                return res.json({ status: "error", message: "No Time In record found for today." });
            }
            
            console.log(`✅ [TIME OUT SUCCESS] recorded for User ${userId}`);
            res.json({ status: "success", message: "Time Out recorded successfully!" });
        });
    }
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`-----------------------------------------`);
    console.log(`🚀 Node.js Payroll Server is LIVE on port ${PORT}`);
    console.log(`-----------------------------------------`);
});