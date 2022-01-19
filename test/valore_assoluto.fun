integer c := 4;
integer d := 5;
fun c(): real
    integer c;
    c := c;
end fun;

fun d(): real
    c();
end fun;

main
    real input := c(), king := input;
    real c := c();
    ?: c;
    % input "Inserire un numero di cui calcolare il valore assoluto";
    if input < 0 then
        input := - input;
    end if;
    ?. input;
end main;