<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" data-bs-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Expense - Expense Tracker</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom Style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--bs-body-bg);
            color: var(--bs-body-color);
        }

        .sidebar {
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            width: 260px;
            z-index: 100;
            background: linear-gradient(180deg, #1e1b4b 0%, #0f172a 100%);
            color: #fff;
            padding-top: 20px;
            transition: all 0.3s ease;
        }

        .sidebar-brand {
            font-size: 1.5rem;
            font-weight: 800;
            padding: 15px 25px;
            background: linear-gradient(to right, #818cf8, #f472b6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            letter-spacing: -0.5px;
            display: block;
            text-decoration: none;
        }

        .sidebar-nav-link {
            display: flex;
            align-items: center;
            padding: 12px 25px;
            color: #94a3b8;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.2s ease;
            border-left: 4px solid transparent;
        }

        .sidebar-nav-link:hover, .sidebar-nav-link.active {
            color: #f8fafc;
            background: rgba(255, 255, 255, 0.05);
            border-left-color: #6366f1;
        }

        .sidebar-nav-link i {
            font-size: 1.2rem;
            margin-right: 15px;
        }

        .main-content {
            margin-left: 260px;
            padding: 30px;
            min-height: 100vh;
        }

        .form-card {
            border-radius: 18px;
            padding: 30px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.02);
            border: 1px solid rgba(0, 0, 0, 0.05);
            background-color: var(--bs-card-bg);
            max-width: 600px;
            margin: 0 auto;
        }

        [data-bs-theme="dark"] .form-card {
            border-color: rgba(255, 255, 255, 0.05);
            background-color: #1e293b;
        }

        .avatar {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            background: linear-gradient(135deg, #818cf8 0%, #c084fc 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 700;
        }

        @media (max-width: 991.98px) {
            .sidebar {
                transform: translateX(-260px);
            }
            .sidebar.show {
                transform: translateX(0);
            }
            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar" id="sidebar">
        <a href="${pageContext.request.contextPath}/dashboard" class="sidebar-brand">
            <i class="bi bi-wallet2 me-2"></i>Expenstrack
        </a>
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/dashboard" class="sidebar-nav-link">
                <i class="bi bi-grid-1x2-fill"></i>Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/expenses" class="sidebar-nav-link active">
                <i class="bi bi-cash-stack"></i>Expenses
            </a>
            <a href="${pageContext.request.contextPath}/jsp/report.jsp" class="sidebar-nav-link">
                <i class="bi bi-file-earmark-bar-graph"></i>Reports
            </a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Navbar -->
        <header class="d-flex justify-content-between align-items-center mb-4">
            <button class="btn btn-outline-secondary d-lg-none" id="sidebarToggle">
                <i class="bi bi-list"></i>
            </button>
            <h4 class="mb-0 fw-bold">Add Expense</h4>
            
            <div class="d-flex align-items-center gap-3">
                <button class="btn btn-link nav-link fs-4" id="themeToggleBtn">
                    <i class="bi bi-moon-stars" id="themeIcon"></i>
                </button>
                <div class="d-flex align-items-center gap-2">
                    <div class="avatar">${sessionScope.user.name.substring(0,1).toUpperCase()}</div>
                    <div class="d-none d-md-block">
                        <div class="fw-bold">${sessionScope.user.name}</div>
                        <div class="text-muted" style="font-size: 0.75rem;">User Account</div>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                    <i class="bi bi-box-arrow-right me-1"></i>Logout
                </a>
            </div>
        </header>

        <!-- Form Card -->
        <div class="form-card">
            <h5 class="fw-bold mb-4">New Expense Transaction</h5>
            
            <form action="${pageContext.request.contextPath}/expenses" method="POST">
                <input type="hidden" name="action" value="add">
                
                <div class="mb-3">
                    <label for="title" class="form-label fw-semibold">Expense Title</label>
                    <input type="text" class="form-control" id="title" name="title" required placeholder="E.g., Grocery Shopping, Uber ride">
                </div>

                <div class="row mb-3">
                    <div class="col-md-6">
                        <label for="amount" class="form-label fw-semibold">Amount ($)</label>
                        <input type="number" class="form-control" id="amount" name="amount" step="0.01" min="0.01" required placeholder="0.00">
                    </div>
                    <div class="col-md-6">
                        <label for="category" class="form-label fw-semibold">Category</label>
                        <select class="form-select" id="category" name="category" required>
                            <option value="" disabled selected>Select category</option>
                            <option value="Food">Food</option>
                            <option value="Travel">Travel</option>
                            <option value="Shopping">Shopping</option>
                            <option value="Bills">Bills</option>
                            <option value="Entertainment">Entertainment</option>
                            <option value="Education">Education</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="expenseDate" class="form-label fw-semibold">Expense Date</label>
                    <input type="date" class="form-control" id="expenseDate" name="expenseDate" required>
                </div>

                <div class="mb-4">
                    <label for="description" class="form-label fw-semibold">Description (Optional)</label>
                    <textarea class="form-control" id="description" name="description" rows="3" placeholder="Add more context details..."></textarea>
                </div>

                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/expenses" class="btn btn-outline-secondary w-50">Cancel</a>
                    <button type="submit" class="btn btn-success w-50">Save Expense</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Script Section -->
    <script>
        // Set default date to today
        document.getElementById('expenseDate').valueAsDate = new Date();

        // Theme Management
        const html = document.documentElement;
        const themeBtn = document.getElementById('themeToggleBtn');
        const themeIcon = document.getElementById('themeIcon');
        
        const savedTheme = localStorage.getItem('theme') || 'light';
        html.setAttribute('data-bs-theme', savedTheme);
        updateThemeIcon(savedTheme);

        themeBtn.addEventListener('click', () => {
            const currentTheme = html.getAttribute('data-bs-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            html.setAttribute('data-bs-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            updateThemeIcon(newTheme);
        });

        function updateThemeIcon(theme) {
            if (theme === 'dark') {
                themeIcon.className = 'bi bi-sun';
            } else {
                themeIcon.className = 'bi bi-moon-stars';
            }
        }

        // Mobile Sidebar Toggle
        const sidebar = document.getElementById('sidebar');
        const sidebarToggle = document.getElementById('sidebarToggle');
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => {
                sidebar.classList.toggle('show');
            });
        }
    </script>
</body>
</html>
