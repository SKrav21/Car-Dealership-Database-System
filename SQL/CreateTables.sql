create table Address
(
  aID int,
  planet varchar(32),
  environment varchar(32),
  street varchar(32),
  primary key (aID)
);


create table Payment
(
  payID int,
  card_number int,
  expiration_date date,
  security_code int,
  company varchar(40),
  primary key (payID)
);


create table Customer
(
  cID int,
  aID int,
  payID int, 
  name_first varchar(32),
  name_last varchar(32),
  email varchar(64),
  phone int,
  primary key (cID),
  foreign key(aID) references Address
        on delete cascade,
  foreign key (payID) references Payment
        on delete cascade
);


create table Service_Location
(
  sID int,
  aID int,
  has_m int,
  has_u int,
  has_s int,
  has_k int,
  primary key (sID),
  foreign key (aID) references Address
        on delete cascade
);


create table Vehicle
(
  VIN int,
  cID int, 
  sID int,
  base_price int,
  miles int,
  year int,
  model varchar(24),
  color varchar(24),
  capacity int,
  showroom int,
  primary key (VIN),
  foreign key(cID) references Customer
        on delete cascade,
  foreign key(sID) references Service_Location
        on delete cascade
);
alter table "SWK324"."VEHICLE" disable  constraint "SYS_C00370794"


create table Package
(
  pkgID int,
  package_name varchar(32),
  price int,
  primary key (pkgID)
 );


create table Upgrade
(
  upgID int,
  upgrade_name varchar(32),
  price int,
  primary key (upgID)
);


create table Vehicle_Upgrade
(
  VIN int,
  upgID int,
  primary key (VIN, upgID),
  foreign key(VIN) references Vehicle
        on delete cascade,
  foreign key(upgID) references Upgrade
        on delete cascade
);


create table Vehicle_Package
(
  VIN int,
  pkgID int,
  primary key (VIN, pkgID),
  foreign key(VIN) references Vehicle
        on delete cascade,
  foreign key(pkgID) references Package
        on delete cascade
);


create table Package_Upgrade
(
  pkgID int,
  upgID int,
  primary key (pkgID, upgID),
  foreign key(pkgID) references Package
        on delete cascade,
  foreign key(upgID) references Upgrade
        on delete cascade
);


create table Maintenance
(
  mID int,
  VIN int,
  sID int,
  payID int,
  cost int,
  maintenance_date date,
  miles int,
  primary key (mID),
  foreign key (VIN) references Vehicle
        on delete cascade,
  foreign key (sID) references Service_Location
        on delete cascade,
  foreign key (payID) references Payment
        on delete cascade
);


create table Transaction
(
  tID int,
  sID int,
  payID int,
  cID int,
  VIN int,
  cost int,
  transaction_date date,
  model varchar(12),
  primary key (tID),
  foreign key (sID) references Service_Location
        on delete cascade,
  foreign key (payID) references Payment
        on delete cascade,
  foreign key (cID) references Customer
        on delete cascade,
  foreign key (VIN) references Vehicle
        on delete cascade
);