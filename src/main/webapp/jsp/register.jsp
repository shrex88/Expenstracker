<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Expense Tracker</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom Style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            color: #f8fafc;
            position: relative;
            overflow: hidden;
        }

        .circle-1 {
            position: absolute;
            width: 300px;
            height: 300px;
            background: radial-gradient(circle, rgba(99, 102, 241, 0.4) 0%, rgba(99, 102, 241, 0) 70%);
            top: -50px;
            left: -50px;
            border-radius: 50%;
            z-index: 1;
        }

        .circle-2 {
            position: absolute;
            width: 400px;
            height: 400px;
            background: radial-gradient(circle, rgba(236, 72, 153, 0.3) 0%, rgba(236, 72, 153, 0) 70%);
            bottom: -100px;
            right: -100px;
            border-radius: 50%;
            z-index: 1;
        }

        .login-container {
            position: relative;
            z-index: 10;
            width: 100%;
            max-width: 440px;
            padding: 20px;
        }

        .glass-card {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(16px);
            -webkit-backdrop-filter: blur(16px);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 24px;
            padding: 40px 30px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .glass-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 25px 50px rgba(99, 102, 241, 0.15);
        }

        .brand-logo {
            font-size: 2.2rem;
            font-weight: 800;
            background: linear-gradient(to right, #818cf8, #f472b6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-align: center;
            margin-bottom: 25px;
            letter-spacing: -0.5px;
        }

        .form-label {
            font-weight: 600;
            color: #94a3b8;
            font-size: 0.9rem;
        }

        .form-control {
            background: rgba(15, 23, 42, 0.6);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 12px;
            color: #f8fafc;
            padding: 12px 16px;
            transition: all 0.2s ease;
        }

        .form-control:focus {
            background: rgba(15, 23, 42, 0.8);
            border-color: #6366f1;
            box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.25);
            color: #f8fafc;
        }

        .btn-success {
            background: linear-gradient(135deg, #10b981 0%, #059669 100%);
            border: none;
            border-radius: 12px;
            padding: 12px;
            font-weight: 600;
            letter-spacing: 0.5px;
            transition: all 0.2s ease;
        }

        .btn-success:hover {
            background: linear-gradient(135deg, #059669 0%, #047857 100%);
            transform: scale(1.02);
            box-shadow: 0 10px 20px rgba(16, 185, 129, 0.3);
        }

        .auth-link {
            color: #818cf8;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.2s;
        }

        .auth-link:hover {
            color: #f472b6;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="circle-1"></div>
    <div class="circle-2"></div>

    <div class="login-container">
        <div class="glass-card">
            <div class="brand-logo">
                <i class="bi bi-wallet2 me-2"></i>Expenstrack
            </div>
            
            <h4 class="text-center mb-4 fw-normal text-white-50">Create Account</h4>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger border-0 bg-danger bg-opacity-25 text-danger rounded-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/register" method="POST">
                <div class="mb-3">
                    <label for="name" class="form-label">FULL NAME</label>
                    <input type="text" class="form-control" id="name" name="name" required placeholder="John Doe">
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">EMAIL ADDRESS</label>
                    <input type="email" class="form-control" id="email" name="email" required placeholder="you@example.com">
                </div>
                
                <div class="mb-4">
                    <label for="password" class="form-label">PASSWORD</label>
                    <input type="password" class="form-control" id="password" name="password" required placeholder="••••••••">
                </div>
                
                <button type="submit" class="btn btn-success w-100 mb-3">
                    Get Started <i class="bi bi-rocket-takeoff ms-2"></i>
                </button>
                
                <div class="text-center mt-4">
                    <span class="text-secondary">Already have an account?</span> 
                    <a href="${pageContext.request.contextPath}/login" class="auth-link ms-1">Sign In</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
