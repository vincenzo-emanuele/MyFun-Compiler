fun leggiOperazione(out string op)
    % op "Scegliere l'operazione da eseguire";
end fun;

fun leggiParametri(out real parametro1, out real parametro2)
    % parametro1 'Inserire il primo parametro';
    % parametro2 'Inserire il secondo parametro';
end fun;

fun calcolaRisultato(real parametro1, real parametro2, string operazione): real
    real result;
    if operazione = '+' then
        real calcolaRisultato := calcolaRisultato(2.8, 2.9, 'ciao');
        result := parametro1 + parametro2;
    end if;
    if operazione = '-' then
        result := parametro1 - parametro2;
    end if;
    if operazione = '*' then
        result := parametro1 * parametro2;
    end if;
    if operazione = '/' then
        result := parametro1/parametro2;
    end if;
    return result;
end fun;

main
    bool scelta := true;
    string op;
    real arg1, arg2, result;
    while scelta != false loop
        leggiOperazione(@op);
        leggiParametri(@arg1, @arg2);
        result := calcolaRisultato(arg1, arg2, op);
        ?, result;
        % scelta "Continuare? (1/0)";
    end loop;
end main;