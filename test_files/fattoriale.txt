integer fattoriale;

fun fattoriale(integer n): integer
    integer fattoriale := 1;
    if n > 0 then
        integer i := 1;
        while i <= n loop
            fattoriale := fattoriale * i;
            i := i + 1;
        end loop;
    end if;
    return fattoriale;
end fun;

main
    integer n := 10;
    integer j := 5;
    ?, n & j & 'Dimmi';
    ?: fattoriale(8);
end main;