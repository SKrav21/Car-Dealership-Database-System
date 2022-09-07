update Maintenance
set maintenance.miles = 
    (Select Vehicle.miles
    from Vehicle
    where Vehicle.VIN = Maintenance.VIN);