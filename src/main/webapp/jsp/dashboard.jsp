<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" data-bs-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Expense Tracker</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom Style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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

        .stats-card {
            border: 1px solid rgba(0, 0, 0, 0.05);
            border-radius: 18px;
            padding: 24px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.02);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.08);
        }

        [data-bs-theme="dark"] .stats-card {
            border-color: rgba(255, 255, 255, 0.05);
            background-color: #1e293b;
        }

        .stats-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            margin-bottom: 15px;
        }

        .icon-blue { background: rgba(99, 102, 241, 0.15); color: #6366f1; }
        .icon-green { background: rgba(16, 185, 129, 0.15); color: #10b981; }
        .icon-yellow { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }
        .icon-purple { background: rgba(139, 92, 246, 0.15); color: #8b5cf6; }

        .chart-card {
            border-radius: 18px;
            padding: 24px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.02);
            border: 1px solid rgba(0, 0, 0, 0.05);
            background-color: var(--bs-card-bg);
            height: 100%;
        }

        [data-bs-theme="dark"] .chart-card {
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
            <a href="${pageContext.request.contextPath}/dashboard" class="sidebar-nav-link active">
                <i class="bi bi-grid-1x2-fill"></i>Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/expenses" class="sidebar-nav-link">
                <i class="bi bi-cash-stack"></i>Expenses
            </a>
            <a href="${pageContext.request.contextPath}/jsp/report.jsp" class="sidebar-nav-link">
                <i class="bi bi-file-earmark-bar-graph"></i>Reports
            </a>
            <hr class="mx-3 my-4 opacity-10">
            <div class="px-4">
                <h6 class="text-uppercase text-muted fw-bold" style="font-size: 0.75rem;">Budget settings</h6>
                <form action="${pageContext.request.contextPath}/dashboard" method="POST" class="mt-2">
                    <div class="input-group input-group-sm mb-2">
                        <span class="input-group-text bg-dark border-secondary text-white">₹</span>
                        <input type="number" class="form-control bg-dark border-secondary text-white" 
                               name="budget" value="${budget}" placeholder="Set budget" step="0.01" required>
                    </div>
                    <button type="submit" class="btn btn-sm btn-primary w-100">Update Budget</button>
                </form>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Navbar -->
        <header class="d-flex justify-content-between align-items-center mb-4">
            <button class="btn btn-outline-secondary d-lg-none" id="sidebarToggle">
                <i class="bi bi-list"></i>
            </button>
            <h4 class="mb-0 fw-bold">Overview</h4>
            
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

        <!-- Budget Alert -->
        <c:if test="${budgetExceeded}">
            <div class="alert alert-danger border-0 bg-danger bg-opacity-10 text-danger rounded-4 p-4 mb-4 d-flex align-items-center gap-3">
                <i class="bi bi-exclamation-octagon-fill fs-3"></i>
                <div>
                    <h6 class="alert-heading fw-bold mb-1">Budget Alert!</h6>
                    <span>You have exceeded your monthly budget of <strong>₹${budget}</strong>. Your current spending is <strong>₹${monthly}</strong>.</span>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty budget && not budgetExceeded}">
            <div class="alert alert-info border-0 bg-info bg-opacity-10 text-info rounded-4 p-3 mb-4 d-flex align-items-center gap-2">
                <i class="bi bi-info-circle-fill"></i>
                <span>Monthly spending is at <strong>₹${monthly}</strong> of your <strong>₹${budget}</strong> budget.</span>
            </div>
        </c:if>

        <!-- Stats Cards Grid -->
        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-blue">
                        <i class="bi bi-currency-rupee"></i>
                    </div>
                    <div class="text-muted fw-medium" style="font-size: 0.85rem;">TOTAL EXPENSES</div>
                    <h3 class="fw-bold mt-1 mb-0">₹<c:out value="${total}" default="0.00" /></h3>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-green">
                        <i class="bi bi-calendar-event"></i>
                    </div>
                    <div class="text-muted fw-medium" style="font-size: 0.85rem;">THIS MONTH</div>
                    <h3 class="fw-bold mt-1 mb-0">₹<c:out value="${monthly}" default="0.00" /></h3>
                </div>
            </div>

            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-yellow">
                        <i class="bi bi-graph-up-arrow"></i>
                    </div>
                    <div class="text-muted fw-medium" style="font-size: 0.85rem;">HIGHEST TRANSACTION</div>
                    <h3 class="fw-bold mt-1 mb-0">
                        <c:choose>
                            <c:when test="${not empty highest}">
                                ₹${highest.amount}
                            </c:when>
                            <c:otherwise>
                                ₹0.00
                            </c:otherwise>
                        </c:choose>
                    </h3>
                    <div class="text-muted" style="font-size: 0.75rem; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
                        <c:out value="${highest.title}" default="No expenses" />
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-purple">
                        <i class="bi bi-wallet2"></i>
                    </div>
                    <div class="text-muted fw-medium" style="font-size: 0.85rem;">MONTHLY BUDGET</div>
                    <h3 class="fw-bold mt-1 mb-0">
                        <c:choose>
                            <c:when test="${not empty budget}">
                                ₹${budget}
                            </c:when>
                            <c:otherwise>
                                Not Set
                            </c:otherwise>
                        </c:choose>
                    </h3>
                </div>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-7">
                <div class="chart-card">
                    <h5 class="fw-bold mb-4">Spending Trends (Last 6 Months)</h5>
                    <div style="position: relative; height: 300px; width: 100%;">
                        <canvas id="trendChart"></canvas>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-5">
                <div class="chart-card">
                    <h5 class="fw-bold mb-4">Category-Wise Breakdown</h5>
                    <div style="position: relative; height: 300px; width: 100%;">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recent Transactions -->
        <div class="chart-card mb-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="fw-bold mb-0">Recent Transactions</h5>
                <a href="${pageContext.request.contextPath}/expenses" class="btn btn-link text-decoration-none">View All</a>
            </div>
            
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>TITLE</th>
                            <th>CATEGORY</th>
                            <th>DATE</th>
                            <th class="text-end">AMOUNT</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${recent}">
                            <tr>
                                <td>
                                    <div class="fw-semibold">${e.title}</div>
                                    <small class="text-muted">${e.description}</small>
                                </td>
                                <td>
                                    <span class="badge rounded-pill bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25 px-3 py-2 text-capitalize">
                                        ${e.category}
                                    </span>
                                </td>
                                <td>${e.expenseDate}</td>
                                <td class="text-end fw-bold text-danger">-₹${e.amount}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recent}">
                            <tr>
                                <td colspan="4" class="text-center py-4 text-muted">
                                    <i class="bi bi-wallet2 fs-2 d-block mb-2"></i>
                                    No transactions found. Add some expenses to get started!
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Script Section -->
    <script>
        // Theme Management
        const html = document.documentElement;
        const themeBtn = document.getElementById('themeToggleBtn');
        const themeIcon = document.getElementById('themeIcon');
        
        // Init theme from storage
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

        // Sidebar Toggle for Mobile
        const sidebar = document.getElementById('sidebar');
        const sidebarToggle = document.getElementById('sidebarToggle');
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => {
                sidebar.classList.toggle('show');
            });
        }

        // --- Render Charts using JSP generated values ---
        // Category Chart Data
        const categoryLabels = [
            <c:forEach var="entry" items="${categorySummary}">
                "${entry.key}",
            </c:forEach>
        ];
        const categoryData = [
            <c:forEach var="entry" items="${categorySummary}">
                ${entry.value},
            </c:forEach>
        ];

        // Monthly Trend Data
        const trendLabels = [
            <c:forEach var="entry" items="${monthlyTrend}">
                "${entry.key}",
            </c:forEach>
        ];
        const trendData = [
            <c:forEach var="entry" items="${monthlyTrend}">
                ${entry.value},
            </c:forEach>
        ];

        // 1. Category Pie Chart
        const catCtx = document.getElementById('categoryChart').getContext('2d');
        new Chart(catCtx, {
            type: 'doughnut',
            data: {
                labels: categoryLabels,
                datasets: [{
                    data: categoryData,
                    backgroundColor: [
                        '#4f46e5', // Food
                        '#10b981', // Travel
                        '#f59e0b', // Shopping
                        '#ef4444', // Bills
                        '#8b5cf6', // Entertainment
                        '#06b6d4', // Education
                        '#64748b'  // Other
                    ],
                    borderWidth: 2,
                    borderColor: 'rgba(255,255,255,0.05)'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: '#94a3b8',
                            font: { family: 'Outfit', size: 12 }
                        }
                    }
                }
            }
        });

        // 2. Trend Bar Chart
        const trendCtx = document.getElementById('trendChart').getContext('2d');
        new Chart(trendCtx, {
            type: 'bar',
            data: {
                labels: trendLabels,
                datasets: [{
                    label: 'Expenses (₹)',
                    data: trendData,
                    backgroundColor: 'rgba(99, 102, 241, 0.85)',
                    hoverBackgroundColor: '#4f46e5',
                    borderRadius: 8,
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { color: '#94a3b8', font: { family: 'Outfit' } }
                    },
                    y: {
                        grid: { color: 'rgba(148, 163, 184, 0.1)' },
                        ticks: { color: '#94a3b8', font: { family: 'Outfit' } }
                    }
                }
            }
        });
    </script>
</body>
</html>
