main
    real input;
    % input "Inserire un numero di cui calcolare il valore assoluto";
    if input < 0 then
        input := - input;
    end if;
    ?. input;
end main;