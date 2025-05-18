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
    CreatedAt DATETIME DEFAULT GETDATE()
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
(5, N'Tường Dealer', 'dealer1', 'dealer123', 'duongquocdagtuong@gmail.com', '0909567890', N'789 Đường Cách Mạng, TP.HCM', 200000);

INSERT INTO Farm (SellerID, FarmName, Location, Description, Status, Note, ImageURL)
VALUES
(4, N'Trại Heo Miền Tây', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Miền Bắc', N'Hà Nội', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Miền Trung', N'Đà Nẵng', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Miền Nam', N'Đồng Nai', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Active',N'Đã xác nhận', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Miền Tây Bắc', N'Sa Pa', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Giống', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/27gqbwCY/farm-default.jpg'),
(4, N'Trại Heo Thịt', N'Bến Tre', N'Trại chuyên cung cấp heo giống chất lượng cao.', 'Pending',N'Đang chờ', 'https://i.ibb.co/27gqbwCY/farm-default.jpg');

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
    RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate, Note
) 
VALUES
(4, 2, 1, N'Heo Thịt Truyền Thống', N'Heo Lai', 40, 4, 1500000, 3200000, 128000000, 
 N'Heo thịt được nuôi theo phương pháp truyền thống.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-10', '2025-05-10', N'Đã kiểm định'),
(4, 2, 2, N'Heo Giống Móng Cái', N'Móng Cái', 30, 3, 2500000, 5000000, 150000000, 
 N'Heo giống Móng Cái thuần chủng, dễ nuôi, sinh sản tốt.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-12', '2025-05-12', N'Đã kiểm định'),
(4, 2, 3, N'Heo Rừng Lai F1', N'Heo Rừng Lai', 20, 2, 3000000, 6000000, 120000000, 
 N'Heo rừng lai F1, chất lượng thịt thơm ngon.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-13', '2025-05-13', N'Đã kiểm định'),
(4, 2, 4, N'Heo Siêu Nạc Hòa Phát', N'Heo Siêu Nạc', 60, 6, 2000000, 4000000, 240000000, 
 N'Heo siêu nạc tăng trưởng nhanh, tỉ lệ nạc cao.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-14', '2025-05-14', N'Đã kiểm định'),
(4, 2, 5, N'Heo Mẹ Landrace', N'Landrace', 15, 2, 3500000, 7000000, 105000000, 
 N'Heo mẹ Landrace có khả năng sinh sản cao.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-15', '2025-05-15', N'Đã kiểm định'),
(4, 2, 6, N'Heo Con Siêu Nạc 2 Tuần Tuổi', N'Siêu Nạc', 50, 5, 1500000, 2000000, 100000000, 
 N'Heo con 2 tuần tuổi, khỏe mạnh.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-16', '2025-05-16', N'Đã kiểm định'),
(4, 2, 7, N'Heo Đen Lạng Sơn', N'Heo Đen', 25, 3, 2500000, 4500000, 112500000, 
 N'Heo đặc sản Lạng Sơn, thịt chắc ngọt.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-17', '2025-05-17', N'Đã kiểm định'),
(4, 3, 1, N'Heo Thịt Trắng Hòa Bình', N'Heo Trắng', 35, 4, 1800000, 3300000, 115500000, 
 N'Heo nuôi tại trại hữu cơ Hòa Bình.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-18', '2025-05-18', N'Đã kiểm định'),
(4, 3, 2, N'Heo Giống Yorkshire', N'Yorkshire', 28, 3, 2700000, 5300000, 148400000, 
 N'Heo giống Yorkshire, tăng trưởng tốt.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-19', '2025-05-19', N'Đã kiểm định'),
(4, 3, 3, N'Heo Rừng Lai Đen', N'Heo Rừng Đen', 22, 2, 2800000, 5800000, 127600000, 
 N'Heo rừng đen chất lượng thịt cao.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-20', '2025-05-20', N'Đã kiểm định'),
(4, 3, 4, N'Heo Siêu Nạc CP', N'CP', 55, 6, 2100000, 4100000, 225500000, 
 N'Heo siêu nạc do CP cung cấp giống.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-21', '2025-05-21', N'Đã kiểm định'),
(4, 3, 5, N'Heo Mẹ Yorkshire', N'Yorkshire', 18, 2, 3300000, 6800000, 122400000, 
 N'Heo mẹ khỏe mạnh, khả năng đẻ nhiều.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-22', '2025-05-22', N'Đã kiểm định'),
(4, 4, 6, N'Heo Con 3 Tuần Tuổi', N'Heo Trắng', 45, 5, 1600000, 2200000, 99000000, 
 N'Heo con đã tách mẹ, sẵn sàng xuất bán.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-23', '2025-05-23', N'Đã kiểm định'),
(4, 4, 7, N'Heo Đặc Sản Quảng Nam', N'Heo Mán', 30, 4, 2600000, 4600000, 138000000, 
 N'Heo địa phương nuôi tại miền núi Quảng Nam.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-24', '2025-05-24', N'Đã kiểm định'),
(4, 4, 1, N'Heo Thịt Lai Lốc', N'Lai Lốc', 38, 5, 1750000, 3250000, 123500000, 
 N'Heo lai lốc chất lượng cao.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-25', '2025-05-25', N'Đã kiểm định'),
(4, 4, 2, N'Heo Giống Duroc', N'Duroc', 26, 3, 2800000, 5400000, 140400000, 
 N'Heo giống Duroc đực giống chuyên dùng lai tạo.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-26', '2025-05-26', N'Đã kiểm định'),
(4, 5, 3, N'Heo Rừng Lai Nọc', N'Heo Rừng Nọc', 12, 2, 3200000, 6200000, 74400000, 
 N'Heo rừng nọc chất lượng tốt, giống khỏe.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-27', '2025-05-27', N'Đã kiểm định'),
(4, 5, 4, N'Heo Siêu Nạc Giống Thái', N'Giống Thái', 60, 6, 1950000, 3900000, 234000000, 
 N'Heo siêu nạc giống Thái Lan.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-28', '2025-05-28', N'Đã kiểm định'),
(4, 6, 5, N'Heo Mẹ F1', N'Lai F1', 20, 2, 3400000, 6900000, 138000000, 
 N'Heo mẹ lai F1, sinh sản tốt.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-29', '2025-05-29', N'Đã kiểm định'),
(4, 6, 6, N'Heo Con CP', N'CP', 48, 4, 1700000, 2300000, 110400000, 
 N'Heo con CP, chất lượng ổn định.', 'https://i.ibb.co/SXtVYJ12/default-pig.jpg', '2025-04-30', '2025-05-30', N'Đã kiểm định');

INSERT INTO Cart (UserID, OfferID, Quantity)
VALUES
(5, 1, 10),  
(5, 2, 2);  


INSERT INTO Orders (DealerID, SellerID, FarmID, OfferID, Quantity, TotalPrice, Status, CreatedAt, ProcessedDate, Note)
VALUES
(5, 4, 2, 2, 5, 25000000, 'Processing', GETDATE(), GETDATE(), N'Đơn đang được xử lý'),
(5, 4, 2, 3, 8, 46400000, 'Pending',   GETDATE(), NULL, N'Đơn chờ xác nhận'),
(5, 4, 2, 4, 6, 24600000, 'Processing', GETDATE(), GETDATE(), N'Đơn đang được xử lý'),
(5, 4, 2, 5, 7, 24600000, 'Processing',   GETDATE(), NULL, N'Đơn đang được xử lý'),
(5, 4, 2, 6, 10, 24600000, 'Deposited', GETDATE(), GETDATE(), N'Đơn đã đặt cọc');

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


INSERT INTO Delivery (OrderID, SellerID, DealerID, DeliveryStatus, RecipientName, Phone, Quantity, TotalPrice, Comments)
VALUES
(1, 4, 5, 'Pending', 'John Doe', '0909123456', 5, 25000000, 'Delivery in progress for confirmed order'),
(2, 4, 5, 'Delivered', 'Jane Smith', '0909988776', 8, 46400000, 'Order delivered to the dealer'),
(3, 4, 5, 'Pending', 'Alice Johnson', '0911223344', 6, 24600000, 'Delivery in progress for confirmed order'),
(4, 4, 5, 'Pending', 'Bob Brown', '0911555666', 7, 24600000, 'Waiting for shipment approval'),
(5, 4, 5, 'Delivered', 'Charlie Wilson', '0909778899', 10, 24600000, 'Order has been delivered to the dealer');

 

INSERT INTO Application (UserID, Content, Reply, Status, SentAt, ProcessingDate, FilePath)
VALUES
(1, N'Yêu cầu mở rộng quy mô trang trại', N'Yêu cầu của bạn đã được phê duyệt. Bạn có thể tiến hành mở rộng.', N'Đã phê duyệt', GETDATE(), GETDATE(), 'expand_farm.pdf'),
(2, N'Yêu cầu thay đổi nhà cung cấp do gặp sự cố chậm trễ', N'Yêu cầu bị từ chối. Vui lòng cung cấp thêm tài liệu liên quan đến sự chậm trễ của nhà cung cấp.', N'Đã từ chối', GETDATE(), GETDATE(), 'supplier_delay.pdf'),
(3, N'Yêu cầu kiểm tra lại đơn hàng', NULL, N'Đã phê duyệt', GETDATE(), GETDATE(), 'KiemTra_DonHang_YeuCau.pdf'),
(4, N'Yêu cầu bổ sung nguồn thức ăn chăn nuôi', NULL, N'Đã từ chối', GETDATE(), GETDATE(), NULL),
(5, N'Yêu cầu hỗ trợ tiếp thị', NULL, N'Đang chờ xử lý', GETDATE(), NULL, 'marketing_support.pdf'),
(5, N'Yêu cầu thông tin về chương trình hợp tác', NULL, N'Đang chờ xử lý', GETDATE(), NULL, NULL);

 

