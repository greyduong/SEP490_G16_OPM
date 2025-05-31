USE master;
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'OPM')
BEGIN
    ALTER DATABASE OPM SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE OPM;
END
GO

CREATE DATABASE OPM;
GO

USE OPM;
GO

-- Drop tables in reverse order of dependency
DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS Application;
DROP TABLE IF EXISTS Statistic;
DROP TABLE IF EXISTS Delivery;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS PigsOffer;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS FarmRequest;
DROP TABLE IF EXISTS Farm;
DROP TABLE IF EXISTS UserAccount;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS WalletTopupHistory;

-- Recreate tables
CREATE TABLE Role (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE UserAccount (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    RoleID INT FOREIGN KEY REFERENCES Role(RoleID),
    FullName NVARCHAR(100) NOT NULL,
    Username NVARCHAR(50) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    Address NVARCHAR(255),
    Wallet DECIMAL(15,2) DEFAULT 0,
    Status NVARCHAR(20) DEFAULT 'Active'
);

CREATE TABLE WalletTopupHistory (
	TransactionID INT PRIMARY KEY IDENTITY(1,1),
	UserID INT FOREIGN KEY REFERENCES UserAccount(UserID),
	Amount MONEY NOT NULL,
	TxnRef NVARCHAR(100) NOT NULL,
	Status NVARCHAR(100) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE WalletUseHistory (
	TransactionID INT PRIMARY KEY IDENTITY(1,1),
	UserID INT FOREIGN KEY REFERENCES UserAccount(UserID),
	Amount MONEY NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
    Note NVARCHAR(MAX)
);

CREATE TABLE Farm (
    FarmID INT PRIMARY KEY IDENTITY(1,1),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    FarmName NVARCHAR(100) NOT NULL,
    Location NVARCHAR(255),	
    Description NVARCHAR(MAX),
    Note NVARCHAR(MAX),
	ImageURL NVARCHAR(500),
    Status NVARCHAR(20) DEFAULT 'Active',
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE Category (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Description NVARCHAR(MAX)
);

CREATE TABLE PigsOffer (
    OfferID INT PRIMARY KEY IDENTITY(1,1),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    FarmID INT FOREIGN KEY REFERENCES Farm(FarmID),
    CategoryID INT FOREIGN KEY REFERENCES Category(CategoryID),
    Name NVARCHAR(255),
    PigBreed NVARCHAR(100),
    Quantity INT,
    MinQuantity INT,
    MinDeposit DECIMAL(15,2),
    RetailPrice DECIMAL(15,2),
    TotalOfferPrice DECIMAL(15,2),
    Description NVARCHAR(MAX),
    ImageURL NVARCHAR(500),
    StartDate DATE,
    EndDate DATE,
    Status NVARCHAR(20) DEFAULT 'Available',
	Note NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE()
);


CREATE TABLE Cart (
    CartID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    OfferID INT FOREIGN KEY REFERENCES PigsOffer(OfferID),
    Quantity INT

);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    Amount DECIMAL(15,2),
    Type NVARCHAR(50), 
    Status NVARCHAR(20) DEFAULT 'Pending',
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    DealerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    FarmID INT FOREIGN KEY REFERENCES Farm(FarmID),
    OfferID INT FOREIGN KEY REFERENCES PigsOffer(OfferID),
    Quantity INT,
    TotalPrice DECIMAL(15,2),
    Status NVARCHAR(20) DEFAULT 'Pending',
    CreatedAt DATETIME DEFAULT GETDATE(),
    ProcessedDate DATETIME DEFAULT GETDATE(),
	Note NVARCHAR(MAX) NULL
);

CREATE TABLE Delivery (
    DeliveryID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),  
    DealerID INT FOREIGN KEY REFERENCES UserAccount(UserID),  
    DeliveryStatus NVARCHAR(50) DEFAULT 'Pending', 
    RecipientName NVARCHAR(100),
	Phone NVARCHAR(20),
    Quantity INT, 
    TotalPrice DECIMAL(15, 2), 
    CreatedAt DATETIME DEFAULT GETDATE(), 
    Comments NVARCHAR(MAX)  
);


CREATE TABLE Statistic (
    StatisticID INT PRIMARY KEY IDENTITY(1,1),
    FarmID INT FOREIGN KEY REFERENCES Farm(FarmID),
    Month INT,
    Year INT,
    TotalOrders INT,
    Revenue DECIMAL(15,2),
);

CREATE TABLE Application (
    ApplicationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    Content NVARCHAR(MAX),
    Reply NVARCHAR(MAX) NULL,  -- Make Reply nullable to allow empty replies
    Status NVARCHAR(20) DEFAULT 'Pending',
    SentAt DATETIME DEFAULT GETDATE(),
    ProcessingDate DATETIME NULL,  -- Add ProcessingDate to track processing status
    FilePath NVARCHAR(255) NULL    -- Add FilePath column to store file path
);

CREATE TABLE Rating (
    RatingID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    DealerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    RatingValue INT CHECK (RatingValue BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE ServerLog (
	LogID INT PRIMARY KEY IDENTITY(1,1),
	Content NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Insert sample data
INSERT INTO Role (RoleName) 
VALUES
(N'Admin'),
(N'Manager'),
(N'Staff'),
(N'Seller'),
(N'Dealer');

INSERT INTO UserAccount (RoleID, FullName, Username, Password, Email, Phone, Address, Wallet)
VALUES
(1, N'Admin ', 'admin', 'admin123', 'admin@gmail.com', '0909123456', N'123 Đường Lê Lợi, TP.HCM', 1000000),
(2, N'Quản lí 1', 'manager1', 'manager123', 'manager1@gmail.com', '0909765432', N'654 Đường Hai Bà Trưng, TP.HCM', 800000),
(3, N'Nhân viên 1', 'staff1', 'staff123', 'staff1@gmail.com', '0909678456', N'321 Đường Trần Hưng Đạo, TP.HCM', 300000),
(4, N'Tuấn Seller', 'seller1', 'seller123', 'duongquocdagtuong@gmail.com', '0909345678', N'456 Đường Nguyễn Huệ, TP.HCM', 500000),
(5, N'Tường Dealer', 'dealer1', 'dealer123', 'duongquocdagtuong@gmail.com', '0909567890', N'789 Đường Cách Mạng, TP.HCM', 200000),
(4, N'Seller 2', 'seller2', 'seller123', 'onlinepigmarket@gmail.com', '0911000001', N'12 Đường Số 1, TP.HCM', 400000),
(4, N'Seller 3', 'seller3', 'seller123', 'onlinepigmarket@gmail.com', '0911000002', N'34 Đường Số 2, TP.HCM', 450000),
(4, N'Seller 4', 'seller4', 'seller123', 'onlinepigmarket@gmail.com', '0911000003', N'56 Đường Số 3, TP.HCM', 420000),
(4, N'Seller 5', 'seller5', 'seller123', 'onlinepigmarket@gmail.com', '0911000004', N'78 Đường Số 4, TP.HCM', 480000),
(5, N'Dealer 2', 'dealer2', 'dealer123', 'onlinepigmarket@gmail.com', '0922000001', N'22 Đường Số 5, TP.HCM', 250000),
(5, N'Dealer 3', 'dealer3', 'dealer123', 'onlinepigmarket@gmail.com', '0922000002', N'44 Đường Số 6, TP.HCM', 300000),
(5, N'Dealer 4', 'dealer4', 'dealer123', 'onlinepigmarket@gmail.com', '0922000003', N'66 Đường Số 7, TP.HCM', 270000),
(5, N'Dealer 5', 'dealer5', 'dealer123', 'onlinepigmarket@gmail.com', '0922000004', N'88 Đường Số 8, TP.HCM', 320000);


INSERT INTO Farm (SellerID, FarmName, Location, Description, Status, Note, ImageURL, CreatedAt)
VALUES
(4, N'Trại Heo Miền Tây', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/PZXLPtZz/1396fb2d3ae7.jpg', '2025-4-30'),
(4, N'Trại Heo Miền Bắc', N'Hà Nội', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/84BjLRxg/2b3eb198c412.jpg', '2025-4-30'),
(4, N'Trại Heo Miền Trung', N'Đà Nẵng', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/wF46ccHV/7185be821951.jpg', '2025-4-30'),
(4, N'Trại Heo Miền Nam', N'Đồng Nai', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/8LGgr00L/d21bcf6dbb49.jpg', '2025-4-30'),
(4, N'Trại Heo Miền Tây Bắc', N'Sa Pa', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/JjN2Ttkb/192dcc834423.jpg', '2025-4-30'),
(4, N'Trại Heo Giống', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/d4MMdvdr/70d4f6f213e3.jpg', '2025-4-30'),
(4, N'Trại Heo Thịt', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/Rk6DPvMw/1305fed508e2.jpg', '2025-4-30'),
(4, N'Trại Heo Củ Chi', N'Củ Chi', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/Rp3tS0LB/f521fe0fe469.jpg', '2025-4-30'),
(4, N'Trại Heo Bình Dương', N'Bình Dương', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/39y6mM65/1297c9e0e624.jpg', '2025-4-30'),
(4, N'Trại Heo Tây Ninh', N'Tây Ninh', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/CsNX6sXN/aee924d3491f.jpg', '2025-4-30'),
(4, N'Trại Heo Long An', N'Long An', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/rGpDggyT/81b0ff8309b9.jpg', '2025-4-30'),
--
(6, N'Trại Heo Củ Chi', N'Củ Chi', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/Rp3tS0LB/f521fe0fe469.jpg', '2025-4-30'),
(7, N'Trại Heo Bình Dương', N'Bình Dương', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/39y6mM65/1297c9e0e624.jpg', '2025-4-30'),
(8, N'Trại Heo Tây Ninh', N'Tây Ninh', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/CsNX6sXN/aee924d3491f.jpg', '2025-4-30'),
(9, N'Trại Heo Long An', N'Long An', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active', N'Đã xác nhận', 'https://i.ibb.co/rGpDggyT/81b0ff8309b9.jpg', '2025-4-30');

INSERT INTO Category (Name, Description) 
VALUES
(N'Heo Thịt', N'Heo được nuôi để lấy thịt'),
(N'Heo Giống', N'Heo dùng để nhân giống'),
(N'Heo Rừng', N'Heo có nguồn gốc tự nhiên, thịt săn chắc'),
(N'Heo Siêu Nạc', N'Heo có tỷ lệ nạc cao, phù hợp nuôi công nghiệp'),
(N'Heo Mẹ', N'Heo giống chuyên dùng để sinh sản'),
(N'Heo Con', N'Heo nhỏ mới sinh, dành cho người nuôi giống'),
(N'Heo Đặc Sản', N'Các giống heo địa phương, chất lượng cao');

INSERT INTO PigsOffer (
    SellerID, FarmID, CategoryID, Name, PigBreed, Quantity, MinQuantity, MinDeposit,
    RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate, Note, CreatedAt
) 
VALUES
(4, 2, 1, N'Heo Thịt Truyền Thống', N'Heo Lai', 40, 4, 1500000, 3200000, 128000000, 
 N'Heo thịt được nuôi theo phương pháp truyền thống.', 'https://i.ibb.co/Vpgs0Xj8/644a49bcfe38.jpg', '2025-04-10', '2025-06-10', N'Đã kiểm định', '2025-4-30'),
(4, 2, 2, N'Heo Giống Móng Cái', N'Móng Cái', 30, 3, 2500000, 5000000, 150000000, 
 N'Heo giống Móng Cái thuần chủng, dễ nuôi, sinh sản tốt.', 'https://i.ibb.co/s7PVdQr/1f8258012aef.jpg', '2025-04-12', '2025-06-12', N'Đã kiểm định', '2025-4-30'),
(4, 2, 3, N'Heo Rừng Lai F1', N'Heo Rừng Lai', 20, 2, 3000000, 6000000, 120000000, 
 N'Heo rừng lai F1, chất lượng thịt thơm ngon.', 'https://i.ibb.co/Kjnt9ZVx/1cf74914fe5f.jpg', '2025-04-13', '2025-06-13', N'Đã kiểm định', '2025-4-30'),
(4, 2, 4, N'Heo Siêu Nạc Hòa Phát', N'Heo Siêu Nạc', 60, 6, 2000000, 4000000, 240000000, 
 N'Heo siêu nạc tăng trưởng nhanh, tỉ lệ nạc cao.', 'https://i.ibb.co/gFSbq2zp/da3ea57d8c32.jpg', '2025-04-14', '2025-06-14', N'Đã kiểm định', '2025-4-30'),
(4, 2, 5, N'Heo Mẹ Landrace', N'Landrace', 15, 2, 3500000, 7000000, 105000000, 
 N'Heo mẹ Landrace có khả năng sinh sản cao.', 'https://i.ibb.co/wFb6p93n/3b45292e5c39.jpg', '2025-04-15', '2025-06-15', N'Đã kiểm định', '2025-4-30'),
(4, 2, 6, N'Heo Con Siêu Nạc 2 Tuần Tuổi', N'Siêu Nạc', 50, 5, 1500000, 2000000, 100000000, 
 N'Heo con 2 tuần tuổi, khỏe mạnh.', 'https://i.ibb.co/1Jr0Zbdy/cc1e4cac6ed9.jpg', '2025-04-16', '2025-06-16', N'Đã kiểm định', '2025-4-30'),
(4, 2, 7, N'Heo Đen Lạng Sơn', N'Heo Đen', 25, 3, 2500000, 4500000, 112500000, 
 N'Heo đặc sản Lạng Sơn, thịt chắc ngọt.', 'https://i.ibb.co/pBRRt2sN/405607aebc4f.jpg', '2025-04-17', '2025-06-17', N'Đã kiểm định', '2025-4-30'),
(4, 3, 1, N'Heo Thịt Trắng Hòa Bình', N'Heo Trắng', 35, 4, 1800000, 3300000, 115500000, 
 N'Heo nuôi tại trại hữu cơ Hòa Bình.', 'https://i.ibb.co/DPRyHXkr/75b8ad3dd1e2.jpg', '2025-04-18', '2025-06-18', N'Đã kiểm định', '2025-4-30'),
(4, 3, 2, N'Heo Giống Yorkshire', N'Yorkshire', 28, 3, 2700000, 5300000, 148400000, 
 N'Heo giống Yorkshire, tăng trưởng tốt.', 'https://i.ibb.co/NqrmxcL/3c6a3cf77e8b.jpg', '2025-04-19', '2025-06-19', N'Đã kiểm định', '2025-4-30'),
(4, 3, 3, N'Heo Rừng Lai Đen', N'Heo Rừng Đen', 22, 2, 2800000, 5800000, 127600000, 
 N'Heo rừng đen chất lượng thịt cao.', 'https://i.ibb.co/bMt0PfjT/40bc48bbf348.jpg', '2025-04-20', '2025-06-20', N'Đã kiểm định', '2025-4-30'),
(4, 3, 4, N'Heo Siêu Nạc CP', N'CP', 55, 6, 2100000, 4100000, 225500000, 
 N'Heo siêu nạc do CP cung cấp giống.', 'https://i.ibb.co/S4N17HRB/83169e2dc0e5.jpg', '2025-04-21', '2025-06-21', N'Đã kiểm định', '2025-4-30'),
(4, 3, 5, N'Heo Mẹ Yorkshire', N'Yorkshire', 18, 2, 3300000, 6800000, 122400000, 
 N'Heo mẹ khỏe mạnh, khả năng đẻ nhiều.', 'https://i.ibb.co/Mycpkb3Y/5d9ad5acfee1.png', '2025-04-22', '2025-06-22', N'Đã kiểm định', '2025-4-30'),
(4, 4, 6, N'Heo Con 3 Tuần Tuổi', N'Heo Trắng', 45, 5, 1600000, 2200000, 99000000, 
 N'Heo con đã tách mẹ, sẵn sàng xuất bán.', 'https://i.ibb.co/bjWTdRcd/d3190bb8437d.jpg', '2025-04-23', '2025-06-23', N'Đã kiểm định', '2025-4-30'),
(4, 4, 7, N'Heo Đặc Sản Quảng Nam', N'Heo Mán', 30, 4, 2600000, 4600000, 138000000, 
 N'Heo địa phương nuôi tại miền núi Quảng Nam.', 'https://i.ibb.co/LzDDdg1h/e0ff19128cae.jpg', '2025-04-24', '2025-06-24', N'Đã kiểm định', '2025-4-30'),
(4, 4, 1, N'Heo Thịt Lai Lốc', N'Lai Lốc', 38, 5, 1750000, 3250000, 123500000, 
 N'Heo lai lốc chất lượng cao.', 'https://i.ibb.co/8n08zjRG/8dc6a9480cec.jpg', '2025-04-25', '2025-06-25', N'Đã kiểm định','2025-4-30'),
(4, 4, 2, N'Heo Giống Duroc', N'Duroc', 26, 3, 2800000, 5400000, 140400000, 
 N'Heo giống Duroc đực giống chuyên dùng lai tạo.', 'https://i.ibb.co/wZP4v1W3/a517b5a97d5a.jpg', '2025-04-26', '2025-06-26', N'Đã kiểm định', '2025-4-30'),
(4, 5, 3, N'Heo Rừng Lai Nọc', N'Heo Rừng Nọc', 12, 2, 3200000, 6200000, 74400000, 
 N'Heo rừng nọc chất lượng tốt, giống khỏe.', 'https://i.ibb.co/hRXG69JV/0df5bb225e5e.jpg', '2025-04-27', '2025-06-27', N'Đã kiểm định', '2025-4-30'),
(4, 5, 4, N'Heo Siêu Nạc Giống Thái', N'Giống Thái', 60, 6, 1950000, 3900000, 234000000, 
 N'Heo siêu nạc giống Thái Lan.', 'https://i.ibb.co/v6NjXPB7/9c484cd11f25.jpg', '2025-04-28', '2025-06-28', N'Đã kiểm định', '2025-4-30'),
(4, 6, 5, N'Heo Mẹ F1', N'Lai F1', 20, 2, 3400000, 6900000, 138000000, 
 N'Heo mẹ lai F1, sinh sản tốt.', 'https://i.ibb.co/fYtCv9Qd/f1804a8259e2.jpg', '2025-04-29', '2025-06-29', N'Đã kiểm định', '2025-4-30'),
(4, 6, 6, N'Heo Con CP', N'CP', 48, 4, 1700000, 2300000, 110400000, 
 N'Heo con CP, chất lượng ổn định.', 'https://i.ibb.co/B2rfQTGT/6fdaa8668449.jpg', '2025-04-30', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(4, 12, 1, N'Heo Siêu Nạc Seller2', N'Siêu Nạc', 50, 5, 2000000, 3000000, 150000000,
N'Heo siêu nạc chất lượng từ Củ Chi.', 'https://i.ibb.co/gFSbq2zp/da3ea57d8c32.jpg', '2025-05-27', '2025-05-29', N'Đã kiểm định', '2025-4-30'),
(4, 13, 2, N'Heo Rừng Seller3', N'Heo Rừng Lai', 40, 4, 2500000, 4000000, 160000000,
N'Heo rừng lai khỏe mạnh từ Bình Dương.', 'https://i.ibb.co/bMt0PfjT/40bc48bbf348.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(4, 14, 3, N'Heo Mẹ Seller4', N'Yorkshire', 30, 3, 3000000, 5000000, 150000000,
N'Heo mẹ giống Yorkshire từ Tây Ninh.', 'https://i.ibb.co/NqrmxcL/3c6a3cf77e8b.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(4, 15, 4, N'Heo Con Seller5', N'Heo Trắng', 60, 6, 1800000, 2800000, 168000000,
N'Heo con 3 tuần tuổi từ Long An.', 'https://i.ibb.co/B2rfQTGT/6fdaa8668449.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(6, 12, 1, N'Heo Siêu Nạc Seller2', N'Siêu Nạc', 50, 5, 2000000, 3000000, 150000000,
N'Heo siêu nạc chất lượng từ Củ Chi.', 'https://i.ibb.co/gFSbq2zp/da3ea57d8c32.jpg', '2025-05-31', '2025-05-30', N'Đã kiểm định', '2025-4-30'),
(7, 13, 2, N'Heo Rừng Seller3', N'Heo Rừng Lai', 40, 4, 2500000, 4000000, 160000000,
N'Heo rừng lai khỏe mạnh từ Bình Dương.', 'https://i.ibb.co/bMt0PfjT/40bc48bbf348.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(8, 14, 3, N'Heo Mẹ Seller4', N'Yorkshire', 30, 3, 3000000, 5000000, 150000000,
N'Heo mẹ giống Yorkshire từ Tây Ninh.', 'https://i.ibb.co/NqrmxcL/3c6a3cf77e8b.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30'),
(9, 15, 4, N'Heo Con Seller5', N'Heo Trắng', 60, 6, 1800000, 2800000, 168000000,
N'Heo con 3 tuần tuổi từ Long An.', 'https://i.ibb.co/B2rfQTGT/6fdaa8668449.jpg', '2025-05-31', '2025-06-30', N'Đã kiểm định', '2025-4-30');


INSERT INTO Cart (UserID, OfferID, Quantity)
VALUES
(5, 1, 10),  
(5, 2, 2);  


INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES
(5, 4, 2, 2, 5, 25000000, 'Processing', '2025-04-30', '2025-05-31', N'Đơn đang được xử lý'),
(5, 4, 2, 3, 8, 46400000, 'Pending',   '2025-04-30', NULL, N'Đơn chờ xác nhận'),
(5, 4, 2, 4, 6, 24600000, 'Processing', '2025-04-30', '2025-05-27', N'Đơn đang được xử lý'),
(5, 4, 2, 5, 7, 24600000, 'Processing',  '2025-04-30', NULL, N'Đơn đang được xử lý'),
(5, 4, 2, 6, 10, 24600000, 'Deposited', '2025-04-30', '2025-05-29', N'Đơn đã đặt cọc');

INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES
(5, 4, 2, 3, 3, 15000000, 'Confirmed', '2025-01-10', '2025-01-11', N'Đơn đã được xác nhận'),
(5, 4, 2, 4, 4, 18000000, 'Pending',   '2025-02-20', NULL, N'Đơn chờ xác nhận'),
(5, 4, 2, 5, 2, 10000000, 'Deposited', '2025-03-01', '2025-03-02', N'Đơn đã đặt cọc'),
(5, 4, 2, 6, 8, 30000000, 'Confirmed', '2025-03-21', '2025-03-22', N'Đơn đã được xác nhận'),
(5, 4, 2, 7, 6, 24000000, 'Confirmed', '2025-04-05', '2025-04-06', N'Đơn đã được xác nhận'),
(5, 4, 2, 8, 9, 29000000, 'Pending',   '2025-04-25', NULL, N'Đơn chờ xác nhận'),
(5, 4, 2, 9, 5, 27500000, 'Deposited', '2025-05-03', '2025-05-04', N'Đơn đã đặt cọc'),
(5, 4, 2, 10, 7, 26500000, 'Confirmed','2025-05-07', '2025-05-21', N'Đơn đã được xác nhận'),
(5, 4, 2, 11, 4, 25500000, 'Pending',  '2025-05-15', NULL, N'Đơn chờ xác nhận');

-- Orders for OfferID = 21 (SellerID = 6, FarmID = 12)
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 6, 12, 21, 5, 15000000, 'Pending', '2025-04-27', NULL, N'Đơn đặt từ dealer2'),
(11, 6, 12, 21, 6, 18000000, 'Confirmed', '2025-04-23', '2025-04-30', N'Đơn đặt từ dealer3'),
(12, 6, 12, 21, 4, 12000000, 'Deposited', '2025-04-28', '2025-05-30', N'Đơn đặt từ dealer4'),
(13, 6, 12, 21, 3, 9000000, 'Pending', '2025-04-21', NULL, N'Đơn đặt từ dealer5'),
(10, 6, 12, 21, 7, 21000000, 'Confirmed', '2025-04-21', '2025-04-30', N'Đơn đặt từ dealer2');

-- Orders for OfferID = 22 (SellerID = 7, FarmID = 13)
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 7, 13, 22, 5, 15000000, 'Pending', '2025-04-21', NULL, N'Đơn đặt từ dealer2'),
(11, 7, 13, 22, 6, 18000000, 'Confirmed', '2025-04-12', '2025-04-30', N'Đơn đặt từ dealer3'),
(12, 7, 13, 22, 4, 12000000, 'Deposited', '2025-04-12', '2025-05-30', N'Đơn đặt từ dealer4'),
(13, 7, 13, 22, 3, 9000000, 'Pending', '2025-04-12', NULL, N'Đơn đặt từ dealer5'),
(10, 7, 13, 22, 7, 21000000, 'Confirmed', '2025-04-12', '2025-04-30', N'Đơn đặt từ dealer2');

-- Orders for OfferID = 23 (SellerID = 8, FarmID = 14)
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 8, 14, 23, 5, 15000000, 'Pending', '2025-04-12', NULL, N'Đơn đặt từ dealer2'),
(11, 8, 14, 23, 6, 18000000, 'Confirmed', '2025-04-11', '2025-04-30', N'Đơn đặt từ dealer3'),
(12, 8, 14, 23, 4, 12000000, 'Deposited', '2025-04-10', '2025-04-30', N'Đơn đặt từ dealer4'),
(13, 8, 14, 23, 3, 9000000, 'Pending', '2025-04-16', NULL, N'Đơn đặt từ dealer5'),
(10, 8, 14, 23, 7, 21000000, 'Confirmed', '2025-04-19', '2025-04-30', N'Đơn đặt từ dealer2');

-- Orders for OfferID = 24 (SellerID = 9, FarmID = 15)
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 9, 15, 24, 5, 15000000, 'Pending', '2025-04-20', NULL, N'Đơn đặt từ dealer2'),
(11, 9, 15, 24, 6, 18000000, 'Confirmed', '2025-04-21', '2025-04-30', N'Đơn đặt từ dealer3'),
(12, 9, 15, 24, 4, 12000000, 'Deposited', '2025-04-22', '2025-04-30', N'Đơn đặt từ dealer4'),
(13, 9, 15, 24, 3, 9000000, 'Pending', '2025-04-23', NULL, N'Đơn đặt từ dealer5'),
(10, 9, 15, 24, 7, 21000000, 'Confirmed', '2025-04-22', '2025-04-30', N'Đơn đặt từ dealer2');


-- Orders for OfferID = 25 (SellerID = 4, FarmID = 2)
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 4, 2, 25, 5, 15000000, 'Pending', '2025-04-3', NULL, N'Đơn đặt từ dealer2'),
(11, 4, 2, 25, 6, 18000000, 'Confirmed', '2025-04-30', '2025-05-30', N'Đơn đặt từ dealer3'),
(12, 4, 2, 25, 4, 12000000, 'Deposited', '2025-04-30', '2025-05-30', N'Đơn đặt từ dealer4'),
(13, 4, 2, 25, 3, 9000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer5'),
(10, 4, 2, 25, 7, 21000000, 'Confirmed', '2025-04-30', '2025-04-30', N'Đơn đặt từ dealer2');
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 4, 3, 26, 5, 15000000, 'Pending', '2025-04-27', NULL, N'Đơn đặt từ dealer2'),
(11, 4, 3, 26, 6, 18000000, 'Confirmed', '2025-04-30', '2025-05-20', N'Đơn đặt từ dealer3'),
(12, 4, 3, 26, 4, 12000000, 'Deposited', '2025-04-30', '2025-05-14', N'Đơn đặt từ dealer4'),
(13, 4, 3, 26, 3, 9000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer5'),
(10, 4, 3, 26, 7, 21000000, 'Confirmed', '2025-04-30', '2025-05-12', N'Đơn đặt từ dealer2');
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 4, 4, 27, 5, 15000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer2'),
(11, 4, 4, 27, 6, 18000000, 'Confirmed', '2025-04-30', '2025-05-10', N'Đơn đặt từ dealer3'),
(12, 4, 4, 27, 4, 12000000, 'Deposited', '2025-04-30', '2025-05-09', N'Đơn đặt từ dealer4'),
(13, 4, 4, 27, 3, 9000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer5'),
(10, 4, 4, 27, 7, 21000000, 'Confirmed', '2025-04-30', '2025-05-06', N'Đơn đặt từ dealer2');
INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES 
(10, 4, 5, 28, 5, 15000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer2'),
(11, 4, 5, 28, 6, 18000000, 'Confirmed', '2025-04-30', '2025-05-03', N'Đơn đặt từ dealer3'),
(12, 4, 5, 28, 4, 12000000, 'Deposited', '2025-04-30', '2025-05-16', N'Đơn đặt từ dealer4'),
(13, 4, 5, 28, 3, 9000000, 'Pending', '2025-04-30', NULL, N'Đơn đặt từ dealer5'),
(10, 4, 5, 28, 7, 21000000, 'Confirmed', '2025-04-30', '2025-05-11', N'Đơn đặt từ dealer2');


INSERT INTO Delivery (OrderID, SellerID, DealerID, DeliveryStatus, RecipientName, Phone, Quantity, TotalPrice, Comments)
VALUES
(1, 4, 5, 'Pending', 'John Doe', '0909123456', 5, 25000000, 'Delivery in progress for confirmed order'),
(2, 4, 5, 'Confirmed', 'Jane Smith', '0909988776', 8, 46400000, 'Order delivered to the dealer'),
(3, 4, 5, 'Pending', 'Alice Johnson', '0911223344', 6, 24600000, 'Delivery in progress for confirmed order'),
(4, 4, 5, 'Canceled', 'Bob Brown', '0911555666', 7, 24600000, 'Waiting for shipment approval'),
(5, 4, 5, 'Confirmed', 'Charlie Wilson', '0909778899', 10, 24600000, 'Order has been delivered to the dealer');
-- 2 Deliveries hoàn tất cho OrderID = 17 (đơn của dealer4 đặt từ seller2, đã giao đủ 4 con)
INSERT INTO Delivery (OrderID, SellerID, DealerID, DeliveryStatus, RecipientName, Phone, Quantity, TotalPrice, Comments)
VALUES
(17, 6, 12, 'Confirmed', N'Dealer4', '0909988776', 2, 6000000, N'Giao lần 1 cho đơn hàng đã xác nhận'),
(17, 6, 12, 'Confirmed', N'Dealer4', '0909988776', 2, 6000000, N'Giao lần 2 - hoàn tất, đủ số lượng');

-- Delivery mới giao 2/4 cho OrderID = 22 (đơn của dealer4 đặt từ seller3)
INSERT INTO Delivery (OrderID, SellerID, DealerID, DeliveryStatus, RecipientName, Phone, Quantity, TotalPrice, Comments)
VALUES
(22, 7, 13, 'Confirmed', N'Dealer4', '0911223344', 2, 6000000, N'Giao lần 1 - chưa đủ số lượng (còn 2)');

 

INSERT INTO Application (UserID, Content, Reply, Status, SentAt, ProcessingDate, FilePath)
VALUES
(1, N'Yêu cầu mở rộng quy mô trang trại', N'Yêu cầu của bạn đã được phê duyệt. Bạn có thể tiến hành mở rộng.', N'Đã phê duyệt', '2025-04-30', '2025-05-30', 'expand_farm.pdf'),
(2, N'Yêu cầu thay đổi nhà cung cấp do gặp sự cố chậm trễ', N'Yêu cầu bị từ chối. Vui lòng cung cấp thêm tài liệu liên quan đến sự chậm trễ của nhà cung cấp.', N'Đã từ chối', '2025-04-30', '2025-04-29', 'supplier_delay.pdf'),
(3, N'Yêu cầu kiểm tra lại đơn hàng', NULL, N'Đã phê duyệt', '2025-05-30', '2025-05-30', 'KiemTra_DonHang_YeuCau.pdf'),
(4, N'Yêu cầu bổ sung nguồn thức ăn chăn nuôi', NULL, N'Đã từ chối', '2025-05-11', '2025-05-13', NULL),
(5, N'Yêu cầu hỗ trợ tiếp thị', NULL, N'Đang chờ xử lý', '2025-04-30', NULL, 'marketing_support.pdf'),
(5, N'Yêu cầu thông tin về chương trình hợp tác', NULL, N'Đang chờ xử lý', '2025-05-30', NULL, NULL),
(5, N'Yêu cầu tham gia hội chợ nông nghiệp', NULL, N'Đang chờ xử lý', '2025-05-24', NULL, NULL),
(5, N'Đề nghị hỗ trợ truyền thông sản phẩm', NULL, N'Đang chờ xử lý', '2025-04-29', NULL, NULL),
(5, N'Yêu cầu được tham quan trại mẫu', NULL, N'Đang chờ xử lý', '2025-04-26', NULL, NULL),
(5, N'Đề nghị gửi catalogue sản phẩm mới', NULL, N'Đang chờ xử lý', '2025-05-19', NULL, NULL),
(5, N'Yêu cầu mở rộng khu vực phân phối', NULL, N'Đang chờ xử lý', '2025-04-27', NULL, NULL),
(5, N'Đăng ký hợp tác dài hạn', NULL, N'Đang chờ xử lý', '2025-05-30', NULL, NULL),
(5, N'Yêu cầu thông tin chương trình đào tạo', NULL, N'Đang chờ xử lý', '2025-05-04', NULL, NULL),
(5, N'Đề nghị hỗ trợ vận chuyển đơn hàng lớn', NULL, N'Đang chờ xử lý', '2025-05-26', NULL, NULL),
(5, N'Yêu cầu tư vấn kỹ thuật nuôi heo', NULL, N'Đang chờ xử lý', '2025-05-24', NULL, NULL),
(5, N'Đăng ký chương trình thử nghiệm giống mới', NULL, N'Đang chờ xử lý', '2025-04-24', NULL, NULL);

INSERT INTO ServerLog (Content, CreatedAt)
VALUES (N'Hủy đơn do quá thời gian xử lý','2025-05-29' ),
(N'Hủy đơn do quá thời gian xử lý','2025-05-29' );


