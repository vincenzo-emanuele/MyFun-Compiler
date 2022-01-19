fun somma(real n1, real n2): real
    return n1 + n2;
end fun;

fun prodotto(real n1, integer n2): real
    var i := 1;
    real temp := n1;
    while i < n2 loop
        n1 := n1 + temp;
        i := i + 1;
    end loop;
    return n1;
end fun;

fun divIntera(integer n1, integer n2): integer
    if (n1 < 0 or n2 < 0) then
        return -1;
    end if;
    return n1 div n2;
end fun;

fun potenza(real n1, real n2): real
    return n1^n2;
end fun;

fun fibonacci(integer n): integer
    if n = 0 then
        return 0;
    end if;
    if n = 1 then
        return 1;
    end if;
    return fibonacci(n-1) + fibonacci(n-2);
end fun;

main
    integer op;
    ?. "Scegli un'operazione:";
    ?. "1 Somma";
    ?. "2 Moltiplicazione";
    ?. "3 Divisione intera tra numeri positivi";
    ?. "4 Elevamento a potenza";
    ?. "5 Successione di Fibonacci";
    % op;
    if op = 1 then
        real n1, n2, result;
        % n1 "Inserisci il primo numero reale";
        % n2 "Inserisci il secondo numero reale";
        result := somma(n1, n2);
        ?. result;
    end if;
    if op = 2 then
        real result, n1;
        integer n2;
        % n1 "Inserisci il primo numero (reale)";
        % n2 "Inserisci il secondo numero (intero)";
        result := prodotto(n1, n2);
        ?. result;
    end if;
    if op = 3 then
        integer n1, n2, result;
        % n1 "Inserisci il primo numero (intero)";
        % n2 "Inserisci il secondo numero (intero)";
        result := divIntera(n1, n2);
        ?. result;
    end if;
    if op = 4 then
        real n1, n2, result;
        % n1 "Inserisci il primo numero (reale)";
        % n2 "Inserisci il secondo numero (reale)";
        result := potenza(n1, n2);
        ?. result;
    end if;
    if op = 5 then
        integer n;
        integer result;
        % n "Inserisci un numero (intero)";
        result := fibonacci(n);
        ?. result;
    end if;
    ?. "Ciaoo";
end main;