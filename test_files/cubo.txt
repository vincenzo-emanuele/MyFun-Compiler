fun cubo(out real n)
    n := n^3;
end fun;

main
    real n;
    % n "Inserire un numero reale di cui calcolare il cubo";
    cubo(@n);
    ?. n;
end main;