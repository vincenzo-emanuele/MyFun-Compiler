var i := 4;
var function := -10;

fun function(): integer
    ?. i;
    ?. function;
    if true then
        function := function();
    end if;
end fun;

main
    var i := 5;
    integer d := 5 - (- 10);
    ?. i;
    function();
end main;