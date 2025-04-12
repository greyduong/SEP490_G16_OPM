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
    DeliveryStatus NVARCHAR(50) DEFAULT 'Preparing',
    ShippedAt DATETIME NULL,
    DeliveredAt DATETIME NULL
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
INSERT INTO Role (RoleName) VALUES
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
(4, N'Trần Thị B', 'seller1', 'seller123', 'seller1@gmail.com', '0909345678', N'456 Đường Nguyễn Huệ, TP.HCM', 500000),
(5, N'Phạm Văn C', 'dealer1', 'dealer123', 'dealer1@gmail.com', '0909567890', N'789 Đường Cách Mạng, TP.HCM', 200000);

INSERT INTO Farm (SellerID, FarmName, Location, Description)
VALUES
(2, N'Trại Heo Miền Tây', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.');

INSERT INTO Category (Name, Description)
VALUES
(N'Heo Thịt', N'Heo được nuôi để lấy thịt'),
(N'Heo Giống', N'Heo dùng để nhân giống');

INSERT INTO PigsOffer (
    SellerID, FarmID, CategoryID, Name, PigBreed, Quantity, MinQuantity, MinDeposit, 
    RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate
) VALUES
(2, 1, 1, N'Combo Heo Lai', N'Heo Lai', 50, 5, 1500000, 3200000, 150000000, 
 N'Heo lai 3 máu, trọng lượng 90kg/con.', 
 N'bbbcca48-5015-4daa-abd8-3bab1ea702f8_pic1.jpg', 
 '2025-04-01', '2025-04-30'),

(2, 1, 2, N'Combo Heo Móng Cái', N'Heo Móng Cái', 20, 2, 5000000, 5500000, 110000000, 
 N'Heo giống Móng Cái thuần chủng.', 
 N'10e32b24-6c1a-47e0-a619-d6a50fa39902_pic2.jpg', 
 '2025-04-05', '2025-05-05');

INSERT INTO Cart (UserID, OfferID, Quantity)
VALUES
(5, 1, 10),  
(5, 2, 2);   

INSERT INTO Application (UserID, Content, Reply, Status, SentAt, ProcessingDate, FilePath)
VALUES
(1, 'Request to expand farm capacity', 'Your request has been approved. You may proceed with the expansion.', 'Approved', GETDATE(), GETDATE(), 'expand_farm.pdf'),
(2, 'Request to change supplier due to delay issues', 'Request denied. Please provide more documentation about the supplier’s delay.', 'Rejected', GETDATE(), GETDATE(), 'supplier_delay.pdf'),
(3, 'Request for training course sponsorship', NULL, 'Approved', GETDATE(), GETDATE(), 'training_request.pdf'),
(4, 'Request for additional feed stock', NULL, 'Rejected', GETDATE(), GETDATE(), NULL),
(5, 'Request for marketing support', NULL, 'Pending', GETDATE(), NULL, 'marketing_support.pdf'),
(6, 'Inquiry about partnership program', NULL, 'Pending', GETDATE(), NULL, NULL);