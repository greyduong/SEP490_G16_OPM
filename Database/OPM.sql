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
	Status NVARCHAR(100) NOT NULL
);

CREATE TABLE Farm (
    FarmID INT PRIMARY KEY IDENTITY(1,1),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),
    FarmName NVARCHAR(100) NOT NULL,
    Location NVARCHAR(255),
    Description NVARCHAR(MAX),
    Status NVARCHAR(20) DEFAULT 'Active',
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE FarmRequest (
    RequestID INT PRIMARY KEY IDENTITY(1,1),
    FarmID INT FOREIGN KEY REFERENCES Farm(FarmID),
    RequestedBy INT FOREIGN KEY REFERENCES UserAccount(UserID),
    Status NVARCHAR(20) DEFAULT 'Pending',
    Note NVARCHAR(MAX),
    RequestedAt DATETIME DEFAULT GETDATE()
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
    OfferID INT FOREIGN KEY REFERENCES PigsOffer(OfferID),
    Quantity INT,
    TotalPrice DECIMAL(15,2),
    Status NVARCHAR(20) DEFAULT 'Pending',
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE Delivery (
    DeliveryID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    SellerID INT FOREIGN KEY REFERENCES UserAccount(UserID),  
    DealerID INT FOREIGN KEY REFERENCES UserAccount(UserID),  
    DeliveryStatus NVARCHAR(50) DEFAULT 'Pending', 
    RecipientName NVARCHAR(100), 
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
(1, N'Nguyễn Văn A', 'admin', 'admin123', 'admin@gmail.com', '0909123456', N'123 Đường Lê Lợi, TP.HCM', 1000000),
(2, N'Võ Minh E', 'manager1', 'manager123', 'manager1@gmail.com', '0909765432', N'654 Đường Hai Bà Trưng, TP.HCM', 800000),
(3, N'Lê Thị D', 'staff1', 'staff123', 'staff1@gmail.com', '0909678456', N'321 Đường Trần Hưng Đạo, TP.HCM', 300000),
(4, N'Tuấn', 'seller1', 'seller123', 'duongquocdagtuong@gmail.com', '0909345678', N'456 Đường Nguyễn Huệ, TP.HCM', 500000),
(5, N'Tường', 'dealer1', 'dealer123', 'duongquocdagtuong@gmail.com', '0909567890', N'789 Đường Cách Mạng, TP.HCM', 200000);

INSERT INTO Farm (SellerID, FarmName, Location, Description)
VALUES
(2, N'Trại Heo Miền Tây', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.');
 
INSERT INTO Category (Name, Description) 
VALUES
(N'Heo Thịt', N'Heo được nuôi để lấy thịt'),
(N'Heo Giống', N'Heo dùng để nhân giống'),
(N'Heo Rừng', N'Heo có nguồn gốc tự nhiên, thịt săn chắc'),
(N'Heo Siêu Nạc', N'Heo có tỷ lệ nạc cao, phù hợp nuôi công nghiệp'),
(N'Heo Mẹ', N'Heo giống chuyên dùng để sinh sản'),
(N'Heo Con', N'Heo nhỏ mới sinh, dành cho người nuôi giống'),
(N'Heo Đặc Sản', N'Các giống heo địa phương, chất lượng cao');

INSERT INTO PigsOffer (SellerID, FarmID, CategoryID, Name, PigBreed, Quantity, MinQuantity, MinDeposit,
    RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate) 
VALUES
(4, 1, 1, N'Heo Thịt Truyền Thống', N'Heo Lai', 40, 4, 1500000, 3200000, 128000000, 
 'Heo thịt được nuôi theo phương pháp truyền thống.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-10', '2025-05-10'),
(4, 1, 2, N'Heo Giống Móng Cái', N'Móng Cái', 30, 3, 2500000, 5000000, 150000000, 
 'Heo giống Móng Cái thuần chủng, dễ nuôi, sinh sản tốt.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-12', '2025-05-12'),
(4, 1, 3, N'Heo Rừng Lai F1', N'Heo Rừng Lai', 20, 2, 3000000, 6000000, 120000000, 
 'Heo rừng lai F1, chất lượng thịt thơm ngon.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-13', '2025-05-13'),
(4, 1, 4, N'Heo Siêu Nạc Hòa Phát', N'Heo Siêu Nạc', 60, 6, 2000000, 4000000, 240000000, 
 'Heo siêu nạc tăng trưởng nhanh, tỉ lệ nạc cao.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-14', '2025-05-14'),
(4, 1, 5, N'Heo Mẹ Landrace', N'Landrace', 15, 2, 3500000, 7000000, 105000000, 
 'Heo mẹ Landrace có khả năng sinh sản cao.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-15', '2025-05-15'),
(4, 1, 6, N'Heo Con Siêu Nạc 2 Tuần Tuổi', N'Siêu Nạc', 50, 5, 1500000, 2000000, 100000000, 
 'Heo con 2 tuần tuổi, khỏe mạnh.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-16', '2025-05-16'),
(4, 1, 7, N'Heo Đen Lạng Sơn', N'Heo Đen', 25, 3, 2500000, 4500000, 112500000, 
 'Heo đặc sản Lạng Sơn, thịt chắc ngọt.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-17', '2025-05-17'),
(4, 1, 1, N'Heo Thịt Trắng Hòa Bình', N'Heo Trắng', 35, 4, 1800000, 3300000, 115500000, 
 'Heo nuôi tại trại hữu cơ Hòa Bình.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-18', '2025-05-18'),
(4, 1, 2, N'Heo Giống Yorkshire', N'Yorkshire', 28, 3, 2700000, 5300000, 148400000, 
 'Heo giống Yorkshire, tăng trưởng tốt.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-19', '2025-05-19'),
(4, 1, 3, N'Heo Rừng Lai Đen', N'Heo Rừng Đen', 22, 2, 2800000, 5800000, 127600000, 
 'Heo rừng đen chất lượng thịt cao.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-20', '2025-05-20'),
(4, 1, 4, N'Heo Siêu Nạc CP', N'CP', 55, 6, 2100000, 4100000, 225500000, 
 'Heo siêu nạc do CP cung cấp giống.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-21', '2025-05-21'),
(4, 1, 5, N'Heo Mẹ Yorkshire', N'Yorkshire', 18, 2, 3300000, 6800000, 122400000, 
 'Heo mẹ khỏe mạnh, khả năng đẻ nhiều.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-22', '2025-05-22'),
(4, 1, 6, N'Heo Con 3 Tuần Tuổi', N'Heo Trắng', 45, 5, 1600000, 2200000, 99000000, 
 'Heo con đã tách mẹ, sẵn sàng xuất bán.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-23', '2025-05-23'),
(4, 1, 7, N'Heo Đặc Sản Quảng Nam', N'Heo Mán', 30, 4, 2600000, 4600000, 138000000, 
 'Heo địa phương nuôi tại miền núi Quảng Nam.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-24', '2025-05-24'),
(4, 1, 1, N'Heo Thịt Lai Lốc', N'Lai Lốc', 38, 5, 1750000, 3250000, 123500000, 
 'Heo lai lốc chất lượng cao.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-25', '2025-05-25'),
(4, 1, 2, N'Heo Giống Duroc', N'Duroc', 26, 3, 2800000, 5400000, 140400000, 
 'Heo giống Duroc đực giống chuyên dùng lai tạo.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-26', '2025-05-26'),
(4, 1, 3, N'Heo Rừng Lai Nọc', N'Heo Rừng Nọc', 12, 2, 3200000, 6200000, 74400000, 
 'Heo rừng nọc chất lượng tốt, giống khỏe.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-27', '2025-05-27'),
(4, 1, 4, N'Heo Siêu Nạc Giống Thái', N'Giống Thái', 60, 6, 1950000, 3900000, 234000000, 
 'Heo siêu nạc giống Thái Lan.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-28', '2025-05-28'),
(4, 1, 5, N'Heo Mẹ F1', N'Lai F1', 20, 2, 3400000, 6900000, 138000000, 
 'Heo mẹ lai F1, sinh sản tốt.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-29', '2025-05-29'),
(4, 1, 6, N'Heo Con CP', N'CP', 48, 4, 1700000, 2300000, 110400000, 
 'Heo con CP, chất lượng ổn định.', '10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', '2025-04-30', '2025-05-30');

INSERT INTO Cart (UserID, OfferID, Quantity)
VALUES
(5, 1, 10),  
(5, 2, 2);  

INSERT INTO Orders (DealerID, SellerID, OfferID, Quantity, TotalPrice, Status)
VALUES
(5, 4, 2, 5, 25000000, 'Confirmed'), 
(5, 4, 3, 8, 46400000, 'Pending'), 
(5, 4, 4, 6, 24600000, 'Confirmed'),
(5, 4, 5, 7, 24600000, 'Pending'),
(5, 4, 6, 10, 24600000, 'Deposited');

-- Insert Delivery records for different order statuses
INSERT INTO Delivery (OrderID, SellerID, DealerID, DeliveryStatus, RecipientName, Quantity, TotalPrice, Comments)
VALUES
(1, 4, 5, 'In Transit', 'John Doe', 5, 25000000, 'Delivery in progress for confirmed order'),  -- OrderID 1
(2, 4, 5, 'Delivered', 'Jane Smith', 8, 46400000, 'Order delivered to the dealer'),           -- OrderID 2
(3, 4, 5, 'In Transit', 'Alice Johnson', 6, 24600000, 'Delivery in progress for confirmed order'),  -- OrderID 3
(4, 4, 5, 'Pending', 'Bob Brown', 7, 24600000, 'Waiting for shipment approval'),    -- OrderID 4
(5, 4, 5, 'Delivered', 'Charlie Wilson', 10, 24600000, 'Order has been delivered to the dealer');  -- OrderID 5


 
INSERT INTO Application (UserID, Content, Reply, Status, SentAt, ProcessingDate, FilePath)
VALUES
(1, 'Request to expand farm capacity', 'Your request has been approved. You may proceed with the expansion.', 'Approved', GETDATE(), GETDATE(), 'expand_farm.pdf'),
(2, 'Request to change supplier due to delay issues', 'Request denied. Please provide more documentation about the supplier’s delay.', 'Rejected', GETDATE(), GETDATE(), 'supplier_delay.pdf'),
(3, 'Request for training course sponsorship', NULL, 'Approved', GETDATE(), GETDATE(), 'training_request.pdf'),
(4, 'Request for additional feed stock', NULL, 'Rejected', GETDATE(), GETDATE(), NULL),
(5, 'Request for marketing support', NULL, 'Pending', GETDATE(), NULL, 'marketing_support.pdf'),
(5, 'Inquiry about partnership program', NULL, 'Pending', GETDATE(), NULL, NULL);
 
