create database parkingManagementSystem;
use parkingManagementSystem;

select count(*) as 'bike_count' from parkingHistory where vehicle_type = 'Car';

select * from parkingHistory;

create table parkingLot (
	lot_id int,
    car_num int default 50,
    bike_num int default 120,
    car_fee varchar (20) default '5.000',
    bike_fee varchar (20) default '3.000',
    addition_fee VARCHAR(20) DEFAULT '5.000',
    carMonthlyFee varchar (20) DEFAULT '160.000',
    bikeMonthlyFee VARCHAR (20) DEFAULT '80.000',
    totalCarMTicket int DEFAULT 50,
    totalBikeMTicket int DEFAULT 120
    primary key (lot_id)
);

insert into parkingLot (lot_id) values 
(1);

select * from registedVehicle;

CREATE TABLE registedVehicle (
    res_id INT IDENTITY(1,1) PRIMARY KEY,
    lot_id INT DEFAULT 1,
    vehicle_plate VARCHAR(20) UNIQUE,
    vehicle_type VARCHAR(20) CHECK (vehicle_type IN ('Car', 'Motorbike')),
    ticket_id VARCHAR(20) UNIQUE,
    start_at DATE default GETDATE(),
    end_at DATE default DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) + 1, 0),
    add_by VARCHAR(20),
    CONSTRAINT fk_lot_monthlyTicket FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id),
    CONSTRAINT fk2_registEmp FOREIGN KEY (add_by) REFERENCES employee(emp_id),
);

select count(*) count_car from parkingHistory where vehicle_type = 'Car' and month(exit_time) = ;
select * from parkingHistory where month(exit_time) = month(GETDATE());


select * from parkingHistory;

-- tìm ngày đầu tiên của tháng tiếp theo
select DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) + 1, 0);

CREATE TABLE ticket (
    ticket_id VARCHAR(20) PRIMARY KEY,
    lot_id INT DEFAULT 1,
    vehicle_type VARCHAR(20) CHECK (vehicle_type IN ('Car', 'Motorbike')),
    ticket_type VARCHAR(20) CHECK (ticket_type IN ('Daily', 'Monthly')),
    be_used BIT DEFAULT 0,
    be_registed bit DEFAULT 0,
    FOREIGN KEY (lot_id) REFERENCES parkingLot(lot_id),
);

CREATE TABLE employee (
    emp_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    emp_gender VARCHAR(10) CHECK (emp_gender IN ('Male', 'Female', 'Other')),
    emp_address VARCHAR(50),
    emp_phone VARCHAR(20),
    emp_position VARCHAR(20) DEFAULT 'Parking Checker',
    emp_shift VARCHAR(20) CHECK (emp_shift IN ('6:00 - 14:00', '14:00 - 22:00', '22:00 - 6:00')),
    manager_id VARCHAR(20),
    emp_avatar VARCHAR(255),
    password_login VARCHAR(20) NOT NULL DEFAULT 'password123',
    CONSTRAINT fk_self FOREIGN KEY (manager_id) REFERENCES employee(emp_id)
);

CREATE TABLE parkingVehicle (
    parking_id INT IDENTITY(1,1) PRIMARY KEY,
    lot_id INT DEFAULT 1,
    vehicle_plate VARCHAR(20) UNIQUE,
    vehicle_type VARCHAR(20),
    ticket_id VARCHAR(20) UNIQUE,
    entry_time DATETIME DEFAULT GETDATE(),
    add_by VARCHAR(20),
    picture VARCHAR(255),
    CONSTRAINT fk1_parkingEmp FOREIGN KEY (add_by) REFERENCES employee(emp_id),
    CONSTRAINT fk2_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id)
);

CREATE TABLE parkingHistory (
    parkHis_id INT IDENTITY(1,1) PRIMARY KEY,
    lot_id INT DEFAULT 1,
    vehicle_plate VARCHAR(20),
    vehicle_type VARCHAR(20),
    ticket_id VARCHAR(20),
    entry_time DATETIME,
    exit_time DATETIME,
    add_by VARCHAR(20),
    release_by VARCHAR(20),
    picture VARCHAR(255),
    CONSTRAINT fk2_parkingEmp FOREIGN KEY (add_by) REFERENCES employee(emp_id),
    CONSTRAINT fk3_parkingEmp FOREIGN KEY (release_by) REFERENCES employee(emp_id),
    CONSTRAINT fk4_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id)
);

SELECT *, FORMAT(CAST(entry_time AS DATE), 'yyyy-MM-dd') AS entry_date
FROM parkingHistory
WHERE entry_time = '2024-01-09 06:49:07.303';

SELECT *
FROM parkingHistory
WHERE DAY(entry_time) = 9 OR MONTH(entry_time) = 1 OR YEAR(entry_time) = 2024;

insert into parkingHistory (vehicle_plate,vehicle_type,ticket_id,entry_time,exit_time,add_by,release_by) VALUES
('UANS-1292','Car','DAILYCAR02','2023-05-02 06:49:07.303','2023-05-03 07:20:07.303','EMP003','EMP004');


-- select * from historyRegisted;

-- CREATE TABLE historyRegisted (
--     res_id INT,
--     lot_id INT,
--     vehicle_plate VARCHAR(20),
--     vehicle_type VARCHAR(20),
--     ticket_id VARCHAR(20),
--     start_at DATE,
--     end_at DATE,
--     PRIMARY KEY (res_id),
--     CONSTRAINT fk_historyRegisted FOREIGN KEY (lot_id) REFERENCES parkingLot(lot_id),
--     CONSTRAINT fk_ticket_historyRegisted FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id)
-- );

-- xoá thẻ hết hạn -- không 
-- GO
-- CREATE PROCEDURE sp_DeleteExpiredRegistedVehicle
-- AS
-- BEGIN
--     DELETE FROM registedVehicle
--     WHERE GETDATE() > end_at;

--     INSERT INTO historyRegisted (res_id, lot_id, vehicle_plate, vehicle_type, ticket_id, start_at, end_at)
--     SELECT res_id, lot_id, vehicle_plate, vehicle_type, ticket_id, start_at, end_at
--     FROM deleted;
-- END;

-- test thu
INSERT INTO registedVehicle (lot_id, vehicle_plate, vehicle_type, ticket_id, start_at, end_at)
VALUES (1, 'ABC123', 'Motorbike', 'MONTHLYBIKE03', '2023-12-29', '2024-01-01');

SELECT * from registedVehicle where ticket_id ='MONTHLYBIKE03';

select * from registedVehicle;
select * from parkingVehicle;
select * from parkingHistory;

select * from ticket where ticket_id ='MONTHLYBIKE03';
UPDATE ticket set be_registed = 0 WHERE ticket_id ='MONTHLYBIKE03';


-- constraint
GO
CREATE TRIGGER trg_DeleteParkingVehicle
ON parkingVehicle
AFTER DELETE
AS
BEGIN
    INSERT INTO parkingHistory (lot_id, vehicle_plate, vehicle_type, ticket_id, entry_time, exit_time, add_by, picture)
    SELECT lot_id, vehicle_plate, vehicle_type, ticket_id, entry_time, GETDATE(), add_by, picture
    FROM deleted;
END;

-- cập nhật vé tháng
GO
CREATE TRIGGER trg_RegistedTicket
ON registedVehicle
AFTER INSERT
AS
BEGIN
    UPDATE ticket
    SET be_registed = 1
    FROM inserted i
    WHERE ticket.ticket_id = i.ticket_id;
END;

-- 
GO
CREATE TRIGGER trg_ReleaseTicket
ON registedVehicle
AFTER DELETE
AS
BEGIN
    UPDATE ticket
    SET be_registed = 0
    FROM inserted i
    WHERE ticket.ticket_id = i.ticket_id;
END;

-- cập nhật vé
GO
CREATE TRIGGER trg_UpdateTicketStatus
ON parkingVehicle
AFTER INSERT
AS
BEGIN
    UPDATE ticket
    SET be_used = 1
    FROM inserted i
    WHERE ticket.ticket_id = i.ticket_id;
END;

-- cập nhật vé 
GO
CREATE TRIGGER trg_UpdateTicketIdOnDelete
ON parkingVehicle
AFTER DELETE
AS
BEGIN
    UPDATE ticket
    SET be_used = 0
    FROM deleted d
    WHERE ticket.ticket_id = d.ticket_id;
END;

select * from parkingHistory;

select * from ticket where be_used =1;

select * from parkingVehicle;

--tạo thêm vé ngày
GO
CREATE TRIGGER trg_UpdateCarNum
ON parkingLot
AFTER UPDATE
AS
BEGIN
    DECLARE @OldCarNum INT
    DECLARE @NewCarNum INT

    SELECT @OldCarNum = car_num FROM deleted
    SELECT @NewCarNum = car_num FROM inserted

    IF @NewCarNum > @OldCarNum
    BEGIN
        DECLARE @Counter INT
        SET @Counter = @OldCarNum + 1

        WHILE @Counter <= @NewCarNum
        BEGIN
            DECLARE @TicketID VARCHAR(20)
            SET @TicketID = 'DAILYCAR' + CAST(@Counter AS VARCHAR(5))

            INSERT INTO ticket (ticket_id, ticket_type, vehicle_type)
            VALUES (@TicketID, 'Daily', 'Car')

            SET @Counter = @Counter + 1
        END
    END
END

-- test thu
UPDATE parkingLot
SET car_num = 55
WHERE lot_id = 1;

SELECT * FROM ticket where vehicle_type = 'Car';


-- tạo thêm vé ngày
GO
CREATE TRIGGER trg_UpdateBikeNum
ON parkingLot
AFTER UPDATE
AS
BEGIN
    DECLARE @OldBikeNum INT
    DECLARE @NewBikeNum INT

    SELECT @OldBikeNum = bike_num FROM deleted
    SELECT @NewBikeNum = bike_num FROM inserted

    IF @NewBikeNum > @OldBikeNum
    BEGIN
        DECLARE @Counter INT
        SET @Counter = @OldBikeNum + 1

        WHILE @Counter <= @NewBikeNum
        BEGIN
            DECLARE @TicketID VARCHAR(20)
            SET @TicketID = 'DAILYBIKE' + CAST(@Counter AS VARCHAR(5))

            INSERT INTO ticket (ticket_id, ticket_type, vehicle_type)
            VALUES (@TicketID, 'Daily', 'Motorbike')

            SET @Counter = @Counter + 1
        END
    END
END

-- test thu
UPDATE parkingLot
SET bike_num = 125
WHERE lot_id = 1;

SELECT * from ticket where ticket_id = 'DAILYBIKE122';

-- insert data


INSERT INTO ticket (ticket_id,ticket_type, vehicle_type) VALUES
('DAILYCAR01', 'Daily', 'Car'),('DAILYCAR02', 'Daily', 'Car'),('DAILYCAR03', 'Daily', 'Car'),('DAILYCAR04', 'Daily', 'Car'),('DAILYCAR05', 'Daily', 'Car'),
('DAILYCAR06', 'Daily', 'Car'),('DAILYCAR07', 'Daily', 'Car'),('DAILYCAR08', 'Daily', 'Car'),('DAILYCAR09', 'Daily', 'Car'),('DAILYCAR10', 'Daily', 'Car'),
('DAILYCAR11', 'Daily', 'Car'),('DAILYCAR12', 'Daily', 'Car'),('DAILYCAR13', 'Daily', 'Car'),('DAILYCAR14', 'Daily', 'Car'),('DAILYCAR15', 'Daily', 'Car'),
('DAILYCAR16', 'Daily', 'Car'),('DAILYCAR17', 'Daily', 'Car'),('DAILYCAR18', 'Daily', 'Car'),('DAILYCAR19', 'Daily', 'Car'),('DAILYCAR20', 'Daily', 'Car'),
('DAILYCAR21', 'Daily', 'Car'),('DAILYCAR22', 'Daily', 'Car'),('DAILYCAR23', 'Daily', 'Car'),('DAILYCAR24', 'Daily', 'Car'),('DAILYCAR25', 'Daily', 'Car'),
('DAILYCAR26', 'Daily', 'Car'),('DAILYCAR27', 'Daily', 'Car'),('DAILYCAR28', 'Daily', 'Car'),('DAILYCAR29', 'Daily', 'Car'),('DAILYCAR30', 'Daily', 'Car'),
('DAILYCAR31', 'Daily', 'Car'),('DAILYCAR32', 'Daily', 'Car'),('DAILYCAR33', 'Daily', 'Car'),('DAILYCAR34', 'Daily', 'Car'),('DAILYCAR35', 'Daily', 'Car'),
('DAILYCAR36', 'Daily', 'Car'),('DAILYCAR37', 'Daily', 'Car'),('DAILYCAR38', 'Daily', 'Car'),('DAILYCAR39', 'Daily', 'Car'),('DAILYCAR40', 'Daily', 'Car'),
('DAILYCAR41', 'Daily', 'Car'),('DAILYCAR42', 'Daily', 'Car'),('DAILYCAR43', 'Daily', 'Car'),('DAILYCAR44', 'Daily', 'Car'),('DAILYCAR45', 'Daily', 'Car'),
('DAILYCAR46', 'Daily', 'Car'),('DAILYCAR47', 'Daily', 'Car'),('DAILYCAR48', 'Daily', 'Car'),('DAILYCAR49', 'Daily', 'Car'),('DAILYCAR50', 'Daily', 'Car');

INSERT INTO ticket (ticket_id, ticket_type, vehicle_type)
VALUES
('DAILYBIKE01', 'Daily', 'Motorbike'), ('DAILYBIKE02', 'Daily', 'Motorbike'), ('DAILYBIKE03', 'Daily', 'Motorbike'), ('DAILYBIKE04', 'Daily', 'Motorbike'), ('DAILYBIKE05', 'Daily', 'Motorbike'),
('DAILYBIKE06', 'Daily', 'Motorbike'), ('DAILYBIKE07', 'Daily', 'Motorbike'), ('DAILYBIKE08', 'Daily', 'Motorbike'), ('DAILYBIKE09', 'Daily', 'Motorbike'), ('DAILYBIKE10', 'Daily', 'Motorbike'),
('DAILYBIKE11', 'Daily', 'Motorbike'), ('DAILYBIKE12', 'Daily', 'Motorbike'), ('DAILYBIKE13', 'Daily', 'Motorbike'), ('DAILYBIKE14', 'Daily', 'Motorbike'), ('DAILYBIKE15', 'Daily', 'Motorbike'),
('DAILYBIKE16', 'Daily', 'Motorbike'), ('DAILYBIKE17', 'Daily', 'Motorbike'), ('DAILYBIKE18', 'Daily', 'Motorbike'), ('DAILYBIKE19', 'Daily', 'Motorbike'), ('DAILYBIKE20', 'Daily', 'Motorbike'),
('DAILYBIKE21', 'Daily', 'Motorbike'), ('DAILYBIKE22', 'Daily', 'Motorbike'), ('DAILYBIKE23', 'Daily', 'Motorbike'), ('DAILYBIKE24', 'Daily', 'Motorbike'), ('DAILYBIKE25', 'Daily', 'Motorbike'),
('DAILYBIKE26', 'Daily', 'Motorbike'), ('DAILYBIKE27', 'Daily', 'Motorbike'), ('DAILYBIKE28', 'Daily', 'Motorbike'), ('DAILYBIKE29', 'Daily', 'Motorbike'), ('DAILYBIKE30', 'Daily', 'Motorbike'),
('DAILYBIKE31', 'Daily', 'Motorbike'), ('DAILYBIKE32', 'Daily', 'Motorbike'), ('DAILYBIKE33', 'Daily', 'Motorbike'), ('DAILYBIKE34', 'Daily', 'Motorbike'), ('DAILYBIKE35', 'Daily', 'Motorbike'),
('DAILYBIKE36', 'Daily', 'Motorbike'), ('DAILYBIKE37', 'Daily', 'Motorbike'), ('DAILYBIKE38', 'Daily', 'Motorbike'), ('DAILYBIKE39', 'Daily', 'Motorbike'), ('DAILYBIKE40', 'Daily', 'Motorbike'),
('DAILYBIKE41', 'Daily', 'Motorbike'), ('DAILYBIKE42', 'Daily', 'Motorbike'), ('DAILYBIKE43', 'Daily', 'Motorbike'), ('DAILYBIKE44', 'Daily', 'Motorbike'), ('DAILYBIKE45', 'Daily', 'Motorbike'),
('DAILYBIKE46', 'Daily', 'Motorbike'), ('DAILYBIKE47', 'Daily', 'Motorbike'), ('DAILYBIKE48', 'Daily', 'Motorbike'), ('DAILYBIKE49', 'Daily', 'Motorbike'), ('DAILYBIKE50', 'Daily', 'Motorbike'),
('DAILYBIKE51', 'Daily', 'Motorbike'), ('DAILYBIKE52', 'Daily', 'Motorbike'), ('DAILYBIKE53', 'Daily', 'Motorbike'), ('DAILYBIKE54', 'Daily', 'Motorbike'), ('DAILYBIKE55', 'Daily', 'Motorbike'),
('DAILYBIKE56', 'Daily', 'Motorbike'), ('DAILYBIKE57', 'Daily', 'Motorbike'), ('DAILYBIKE58', 'Daily', 'Motorbike'), ('DAILYBIKE59', 'Daily', 'Motorbike'), ('DAILYBIKE60', 'Daily', 'Motorbike'),
('DAILYBIKE61', 'Daily', 'Motorbike'), ('DAILYBIKE62', 'Daily', 'Motorbike'), ('DAILYBIKE63', 'Daily', 'Motorbike'), ('DAILYBIKE64', 'Daily', 'Motorbike'), ('DAILYBIKE65', 'Daily', 'Motorbike'),
('DAILYBIKE66', 'Daily', 'Motorbike'), ('DAILYBIKE67', 'Daily', 'Motorbike'), ('DAILYBIKE68', 'Daily', 'Motorbike'), ('DAILYBIKE69', 'Daily', 'Motorbike'), ('DAILYBIKE70', 'Daily', 'Motorbike'),
('DAILYBIKE71', 'Daily', 'Motorbike'), ('DAILYBIKE72', 'Daily', 'Motorbike'), ('DAILYBIKE73', 'Daily', 'Motorbike'), ('DAILYBIKE74', 'Daily', 'Motorbike'), ('DAILYBIKE75', 'Daily', 'Motorbike'),
('DAILYBIKE76', 'Daily', 'Motorbike'), ('DAILYBIKE77', 'Daily', 'Motorbike'), ('DAILYBIKE78', 'Daily', 'Motorbike'), ('DAILYBIKE79', 'Daily', 'Motorbike'), ('DAILYBIKE80', 'Daily', 'Motorbike'),
('DAILYBIKE81', 'Daily', 'Motorbike'), ('DAILYBIKE82', 'Daily', 'Motorbike'), ('DAILYBIKE83', 'Daily', 'Motorbike'), ('DAILYBIKE84', 'Daily', 'Motorbike'), ('DAILYBIKE85', 'Daily', 'Motorbike'),
('DAILYBIKE86', 'Daily', 'Motorbike'), ('DAILYBIKE87', 'Daily', 'Motorbike'), ('DAILYBIKE88', 'Daily', 'Motorbike'), ('DAILYBIKE89', 'Daily', 'Motorbike'), ('DAILYBIKE90', 'Daily', 'Motorbike'),
('DAILYBIKE91', 'Daily', 'Motorbike'), ('DAILYBIKE92', 'Daily', 'Motorbike'), ('DAILYBIKE93', 'Daily', 'Motorbike'), ('DAILYBIKE94', 'Daily', 'Motorbike'), ('DAILYBIKE95', 'Daily', 'Motorbike'),
('DAILYBIKE96', 'Daily', 'Motorbike'), ('DAILYBIKE97', 'Daily', 'Motorbike'), ('DAILYBIKE98', 'Daily', 'Motorbike'), ('DAILYBIKE99', 'Daily', 'Motorbike'), ('DAILYBIKE100', 'Daily', 'Motorbike'),
('DAILYBIKE101', 'Daily', 'Motorbike'), ('DAILYBIKE102', 'Daily', 'Motorbike'), ('DAILYBIKE103', 'Daily', 'Motorbike'), ('DAILYBIKE104', 'Daily', 'Motorbike'), ('DAILYBIKE105', 'Daily', 'Motorbike'),
('DAILYBIKE106', 'Daily', 'Motorbike'), ('DAILYBIKE107', 'Daily', 'Motorbike'), ('DAILYBIKE108', 'Daily', 'Motorbike'), ('DAILYBIKE109', 'Daily', 'Motorbike'), ('DAILYBIKE110', 'Daily', 'Motorbike'),
('DAILYBIKE111', 'Daily', 'Motorbike'), ('DAILYBIKE112', 'Daily', 'Motorbike'), ('DAILYBIKE113', 'Daily', 'Motorbike'), ('DAILYBIKE114', 'Daily', 'Motorbike'), ('DAILYBIKE115', 'Daily', 'Motorbike'),
('DAILYBIKE116', 'Daily', 'Motorbike'), ('DAILYBIKE117', 'Daily', 'Motorbike'), ('DAILYBIKE118', 'Daily', 'Motorbike'), ('DAILYBIKE119', 'Daily', 'Motorbike'), ('DAILYBIKE120', 'Daily', 'Motorbike');

INSERT INTO ticket (ticket_id, ticket_type, vehicle_type) VALUES
('MONTHLYCAR01', 'Monthly', 'Car'),('MONTHLYCAR02', 'Monthly', 'Car'),('MONTHLYCAR03', 'Monthly', 'Car'),('MONTHLYCAR04', 'Monthly', 'Car'),('MONTHLYCAR05', 'Monthly', 'Car'),
('MONTHLYCAR06', 'Monthly', 'Car'),('MONTHLYCAR07', 'Monthly', 'Car'),('MONTHLYCAR08', 'Monthly', 'Car'),('MONTHLYCAR09', 'Monthly', 'Car'),('MONTHLYCAR10', 'Monthly', 'Car'),
('MONTHLYCAR11', 'Monthly', 'Car'),('MONTHLYCAR12', 'Monthly', 'Car'),('MONTHLYCAR13', 'Monthly', 'Car'),('MONTHLYCAR14', 'Monthly', 'Car'),('MONTHLYCAR15', 'Monthly', 'Car'),
('MONTHLYCAR16', 'Monthly', 'Car'),('MONTHLYCAR17', 'Monthly', 'Car'),('MONTHLYCAR18', 'Monthly', 'Car'),('MONTHLYCAR19', 'Monthly', 'Car'),('MONTHLYCAR20', 'Monthly', 'Car'),
('MONTHLYCAR21', 'Monthly', 'Car'),('MONTHLYCAR22', 'Monthly', 'Car'),('MONTHLYCAR23', 'Monthly', 'Car'),('MONTHLYCAR24', 'Monthly', 'Car'),('MONTHLYCAR25', 'Monthly', 'Car'),
('MONTHLYCAR26', 'Monthly', 'Car'),('MONTHLYCAR27', 'Monthly', 'Car'),('MONTHLYCAR28', 'Monthly', 'Car'),('MONTHLYCAR29', 'Monthly', 'Car'),('MONTHLYCAR30', 'Monthly', 'Car'),
('MONTHLYCAR31', 'Monthly', 'Car'),('MONTHLYCAR32', 'Monthly', 'Car'),('MONTHLYCAR33', 'Monthly', 'Car'),('MONTHLYCAR34', 'Monthly', 'Car'),('MONTHLYCAR35', 'Monthly', 'Car'),
('MONTHLYCAR36', 'Monthly', 'Car'),('MONTHLYCAR37', 'Monthly', 'Car'),('MONTHLYCAR38', 'Monthly', 'Car'),('MONTHLYCAR39', 'Monthly', 'Car'),('MONTHLYCAR40', 'Monthly', 'Car'),
('MONTHLYCAR41', 'Monthly', 'Car'),('MONTHLYCAR42', 'Monthly', 'Car'),('MONTHLYCAR43', 'Monthly', 'Car'),('MONTHLYCAR44', 'Monthly', 'Car'),('MONTHLYCAR45', 'Monthly', 'Car'),
('MONTHLYCAR46', 'Monthly', 'Car'),('MONTHLYCAR47', 'Monthly', 'Car'),('MONTHLYCAR48', 'Monthly', 'Car'),('MONTHLYCAR49', 'Monthly', 'Car'),('MONTHLYCAR50', 'Monthly', 'Car');

INSERT INTO ticket (ticket_id, ticket_type, vehicle_type) VALUES
('MONTHLYBIKE01', 'Monthly', 'Motorbike'), ('MONTHLYBIKE02', 'Monthly', 'Motorbike'), ('MONTHLYBIKE03', 'Monthly', 'Motorbike'), ('MONTHLYBIKE04', 'Monthly', 'Motorbike'), ('MONTHLYBIKE05', 'Monthly', 'Motorbike'),
('MONTHLYBIKE06', 'Monthly', 'Motorbike'), ('MONTHLYBIKE07', 'Monthly', 'Motorbike'), ('MONTHLYBIKE08', 'Monthly', 'Motorbike'), ('MONTHLYBIKE09', 'Monthly', 'Motorbike'), ('MONTHLYBIKE10', 'Monthly', 'Motorbike'),
('MONTHLYBIKE11', 'Monthly', 'Motorbike'), ('MONTHLYBIKE12', 'Monthly', 'Motorbike'), ('MONTHLYBIKE13', 'Monthly', 'Motorbike'), ('MONTHLYBIKE14', 'Monthly', 'Motorbike'), ('MONTHLYBIKE15', 'Monthly', 'Motorbike'),
('MONTHLYBIKE16', 'Monthly', 'Motorbike'), ('MONTHLYBIKE17', 'Monthly', 'Motorbike'), ('MONTHLYBIKE18', 'Monthly', 'Motorbike'), ('MONTHLYBIKE19', 'Monthly', 'Motorbike'), ('MONTHLYBIKE20', 'Monthly', 'Motorbike'),
('MONTHLYBIKE21', 'Monthly', 'Motorbike'), ('MONTHLYBIKE22', 'Monthly', 'Motorbike'), ('MONTHLYBIKE23', 'Monthly', 'Motorbike'), ('MONTHLYBIKE24', 'Monthly', 'Motorbike'), ('MONTHLYBIKE25', 'Monthly', 'Motorbike'),
('MONTHLYBIKE26', 'Monthly', 'Motorbike'), ('MONTHLYBIKE27', 'Monthly', 'Motorbike'), ('MONTHLYBIKE28', 'Monthly', 'Motorbike'), ('MONTHLYBIKE29', 'Monthly', 'Motorbike'), ('MONTHLYBIKE30', 'Monthly', 'Motorbike'),
('MONTHLYBIKE31', 'Monthly', 'Motorbike'), ('MONTHLYBIKE32', 'Monthly', 'Motorbike'), ('MONTHLYBIKE33', 'Monthly', 'Motorbike'), ('MONTHLYBIKE34', 'Monthly', 'Motorbike'), ('MONTHLYBIKE35', 'Monthly', 'Motorbike'),
('MONTHLYBIKE36', 'Monthly', 'Motorbike'), ('MONTHLYBIKE37', 'Monthly', 'Motorbike'), ('MONTHLYBIKE38', 'Monthly', 'Motorbike'), ('MONTHLYBIKE39', 'Monthly', 'Motorbike'), ('MONTHLYBIKE40', 'Monthly', 'Motorbike'),
('MONTHLYBIKE41', 'Monthly', 'Motorbike'), ('MONTHLYBIKE42', 'Monthly', 'Motorbike'), ('MONTHLYBIKE43', 'Monthly', 'Motorbike'), ('MONTHLYBIKE44', 'Monthly', 'Motorbike'), ('MONTHLYBIKE45', 'Monthly', 'Motorbike'),
('MONTHLYBIKE46', 'Monthly', 'Motorbike'), ('MONTHLYBIKE47', 'Monthly', 'Motorbike'), ('MONTHLYBIKE48', 'Monthly', 'Motorbike'), ('MONTHLYBIKE49', 'Monthly', 'Motorbike'), ('MONTHLYBIKE50', 'Monthly', 'Motorbike'),
('MONTHLYBIKE51', 'Monthly', 'Motorbike'), ('MONTHLYBIKE52', 'Monthly', 'Motorbike'), ('MONTHLYBIKE53', 'Monthly', 'Motorbike'), ('MONTHLYBIKE54', 'Monthly', 'Motorbike'), ('MONTHLYBIKE55', 'Monthly', 'Motorbike'),
('MONTHLYBIKE56', 'Monthly', 'Motorbike'), ('MONTHLYBIKE57', 'Monthly', 'Motorbike'), ('MONTHLYBIKE58', 'Monthly', 'Motorbike'), ('MONTHLYBIKE59', 'Monthly', 'Motorbike'), ('MONTHLYBIKE60', 'Monthly', 'Motorbike'),
('MONTHLYBIKE61', 'Monthly', 'Motorbike'), ('MONTHLYBIKE62', 'Monthly', 'Motorbike'), ('MONTHLYBIKE63', 'Monthly', 'Motorbike'), ('MONTHLYBIKE64', 'Monthly', 'Motorbike'), ('MONTHLYBIKE65', 'Monthly', 'Motorbike'),
('MONTHLYBIKE66', 'Monthly', 'Motorbike'), ('MONTHLYBIKE67', 'Monthly', 'Motorbike'), ('MONTHLYBIKE68', 'Monthly', 'Motorbike'), ('MONTHLYBIKE69', 'Monthly', 'Motorbike'), ('MONTHLYBIKE70', 'Monthly', 'Motorbike'),
('MONTHLYBIKE71', 'Monthly', 'Motorbike'), ('MONTHLYBIKE72', 'Monthly', 'Motorbike'), ('MONTHLYBIKE73', 'Monthly', 'Motorbike'), ('MONTHLYBIKE74', 'Monthly', 'Motorbike'), ('MONTHLYBIKE75', 'Monthly', 'Motorbike'),
('MONTHLYBIKE76', 'Monthly', 'Motorbike'), ('MONTHLYBIKE77', 'Monthly', 'Motorbike'), ('MONTHLYBIKE78', 'Monthly', 'Motorbike'), ('MONTHLYBIKE79', 'Monthly', 'Motorbike'), ('MONTHLYBIKE80', 'Monthly', 'Motorbike'),
('MONTHLYBIKE81', 'Monthly', 'Motorbike'), ('MONTHLYBIKE82', 'Monthly', 'Motorbike'), ('MONTHLYBIKE83', 'Monthly', 'Motorbike'), ('MONTHLYBIKE84', 'Monthly', 'Motorbike'), ('MONTHLYBIKE85', 'Monthly', 'Motorbike'),
('MONTHLYBIKE86', 'Monthly', 'Motorbike'), ('MONTHLYBIKE87', 'Monthly', 'Motorbike'), ('MONTHLYBIKE88', 'Monthly', 'Motorbike'), ('MONTHLYBIKE89', 'Monthly', 'Motorbike'), ('MONTHLYBIKE90', 'Monthly', 'Motorbike'),
('MONTHLYBIKE91', 'Monthly', 'Motorbike'), ('MONTHLYBIKE92', 'Monthly', 'Motorbike'), ('MONTHLYBIKE93', 'Monthly', 'Motorbike'), ('MONTHLYBIKE94', 'Monthly', 'Motorbike'), ('MONTHLYBIKE95', 'Monthly', 'Motorbike'),
('MONTHLYBIKE96', 'Monthly', 'Motorbike'), ('MONTHLYBIKE97', 'Monthly', 'Motorbike'), ('MONTHLYBIKE98', 'Monthly', 'Motorbike'), ('MONTHLYBIKE99', 'Monthly', 'Motorbike'), ('MONTHLYBIKE100', 'Monthly', 'Motorbike'),
('MONTHLYBIKE101', 'Monthly', 'Motorbike'), ('MONTHLYBIKE102', 'Monthly', 'Motorbike'), ('MONTHLYBIKE103', 'Monthly', 'Motorbike'), ('MONTHLYBIKE104', 'Monthly', 'Motorbike'), ('MONTHLYBIKE105', 'Monthly', 'Motorbike'),
('MONTHLYBIKE106', 'Monthly', 'Motorbike'), ('MONTHLYBIKE107', 'Monthly', 'Motorbike'), ('MONTHLYBIKE108', 'Monthly', 'Motorbike'), ('MONTHLYBIKE109', 'Monthly', 'Motorbike'), ('MONTHLYBIKE110', 'Monthly', 'Motorbike'),
('MONTHLYBIKE111', 'Monthly', 'Motorbike'), ('MONTHLYBIKE112', 'Monthly', 'Motorbike'), ('MONTHLYBIKE113', 'Monthly', 'Motorbike'), ('MONTHLYBIKE114', 'Monthly', 'Motorbike'), ('MONTHLYBIKE115', 'Monthly', 'Motorbike'),
('MONTHLYBIKE116', 'Monthly', 'Motorbike'), ('MONTHLYBIKE117', 'Monthly', 'Motorbike'), ('MONTHLYBIKE118', 'Monthly', 'Motorbike'), ('MONTHLYBIKE119', 'Monthly', 'Motorbike'), ('MONTHLYBIKE120', 'Monthly', 'Motorbike');

select * from employee;

INSERT INTO employee (emp_id, first_name, last_name, emp_gender, emp_address, emp_phone, emp_position, emp_shift, manager_id, password_login)
VALUES
    ('EMP001', 'John', 'Doe', 'Male', '123 Main St', '555-1234', 'Parking Manager', '6:00 - 14:00', NULL, 'john_password'),
    ('EMP002', 'Jane', 'Smith', 'Female', '456 Oak St', '555-5678', 'Parking Checker', '14:00 - 22:00', 'EMP001', 'jane_password'),
    ('EMP003', 'Bob', 'Williams', 'Male', '789 Pine St', '555-9101', 'Parking Checker', '22:00 - 6:00', 'EMP001', 'bob_password'),
    ('EMP004', 'Alice', 'Johnson', 'Female', '101 Elm St', '555-1212', 'Parking Checker', '6:00 - 14:00', 'EMP002', 'alice_password');

select * from registedVehicle;


