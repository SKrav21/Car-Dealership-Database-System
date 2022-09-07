update Vehicle
set model = 
CASE 
when 
mod(ROWNUM, 36) = 1 or mod(ROWNUM, 36) = 3 or mod(ROWNUM, 36) = 6 or mod(ROWNUM, 36) = 8 or mod(ROWNUM, 36) = 10 or mod(ROWNUM, 36) = 11 
or mod(ROWNUM, 36) = 25 or mod(ROWNUM, 36) = 27 or mod(ROWNUM, 36) = 30 or mod(ROWNUM, 36) = 32 or mod(ROWNUM, 36) = 34 or mod(ROWNUM, 36) = 35
Then 'k'
when
mod(ROWNUM, 36) = 4 or mod(ROWNUM, 36) = 7 or mod(ROWNUM, 36) = 28 or mod(ROWNUM, 36) = 31
Then 'u'
when 
mod(ROWNUM, 36) = 9 or mod(ROWNUM, 36) = 33
Then 'm'
else 's'
end;

