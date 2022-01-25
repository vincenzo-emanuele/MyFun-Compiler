fun mod_string2(out string str1, out integer integer1, integer integer2)
    ?. "Prima: " & str1;
    str1 := "Modificata da mod_string2";
    ?. "Dopo: " & str1;
    integer1 := -10;
end fun;

fun mod_string(out string str1, out integer integer1, out integer integer2)
    ?. "Prima: " & str1;
    str1 := "Modificata da mod_string";
    mod_string2(str1, integer1, integer2+5);
    ?. "Dopo la chiamata: " & str1;
end fun;

main
    string str1 := "Main";
    integer integer1 := 10;
    integer integer2;
    ?. "Valore di integer1 prima di chiamare mod_string: " & integer1;
    mod_string(@str1, @integer1, @integer2);
    ?. "Valore di integer1 dopo aver chiamato mod_string: " & integer1;
    ?. str1;
end main;