integer mod_string := 20;
integer mod_string2;

fun mod_string2(integer b, out string str2): integer
    ?. "In mod_string2: " & mod_string;
    ?. str2;
end fun;

fun mod_string(out string a): integer
    a := "Ciao";
    mod_string2(2, a);
end fun;

main
    var mod_string := -10;
    var i := 1;
    string input;
    mod_string(@input);
    if true then
        integer mod_string := 5;
        ?. "Nell'if: " & mod_string;
        while i < 5 loop
            var jj := 5;
            if i > 0 then
                ?. i + 1;
                ?. jj;
            end if;
            i := i + 1;
        end loop;
    else
        integer mod_string := 7;
        ?. "Nell'else: " & mod_string;
    end if;
    while i < 5 loop
        integer mod_string := 77;
        ?. "Nel while: " & mod_string;
        i := i + 1;
    end loop;
    ?. input;
    ?. "Alla fine del main: " & mod_string;
    % input, mod_string, i "Str, int, int";
    ?. input & " " & mod_string & ' ' & i;
end main;