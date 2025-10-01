# ✨ Premium Transaction Dashboard

A responsive, glassmorphism-styled **transaction analytics dashboard** built with **HTML, CSS (Tailwind), and JavaScript**.  
It includes KPI cards, interactive charts, a searchable data table, and export features — all working **offline**.

---

## 🌟 Features

- 📊 **Summary KPIs**  
  - Total Transactions  
  - Average Transactions  
  - Minimum & Maximum IDs  

- 📋 **Dynamic Data Table**  
  - Beautifully styled with TailwindCSS  
  - Instant search filter  
  - Hover effects & animations  

- 📈 **Charts & Trends**  
  - Bar Chart (Transaction type comparison)  
  - Pie Chart (End Point distribution)  
  - Powered by [Chart.js](https://www.chartjs.org/)  

- 📂 **Export Options**  
  - Export to **Excel** (`.xlsx`)  
  - Export to **PDF** (with autoTable)  

- 🎨 **Modern Design**  
  - Light theme with premium gradients  
  - Glassmorphism UI  
  - Smooth entry animations  

- ⚡ **Offline Support**  
  - All dependencies included locally (`libs/` folder)  
  - No internet required to run

---

## 🚀 Getting Started

### 1. Clone or Download
```bash
git clone https://github.com/umair-ali-bhutto/simpleTxnDashBoard.git
cd simpleTxnDashBoard
```

### 2. Open the Dashboard

Simply open `index.html` in your browser:

```bash
# Linux / macOS
open index.html

# Windows
start index.html
```

✅ That’s it! The dashboard runs entirely in your browser.

---

## 📂 Project Structure

```
dashboard/
│
├── index.html          # Main dashboard page
├── README.md           # Project documentation
│
└── libs/               # Offline JS/CSS libraries
    ├── tailwind.min.css
    ├── chart.min.js
    ├── xlsx.full.min.js
    ├── jspdf.umd.min.js
    └── jspdf.plugin.autotable.min.js
```

---

## 🛠️ Built With

* [TailwindCSS](https://tailwindcss.com/) – Utility-first CSS framework
* [Chart.js](https://www.chartjs.org/) – Data visualization library
* [SheetJS (XLSX)](https://sheetjs.com/) – Excel export
* [jsPDF](https://github.com/parallax/jsPDF) + [autoTable](https://github.com/simonbengtsson/jsPDF-AutoTable) – PDF export


---

## 📜 License

This project is licensed under the **MIT License** – feel free to use, modify, and share.

---

## 💡 Customization

* To add more transactions, simply edit the `<tbody>` rows in `index.html`.
* To change chart data, update the datasets inside the `<script>` section.
* For large datasets, consider connecting to a backend API.

---

## 🙌 Acknowledgements

Special thanks to the open-source libraries that power this dashboard:

* TailwindCSS
* Chart.js
* SheetJS
* jsPDF & autoTable


