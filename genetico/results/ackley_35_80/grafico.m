clear all

funcname = 'ackley';
pop_size = 35;
generations = 80;


for i = 1:10:30
    num = int2str(i);
    nomearquivo = [ funcname '_run_' num '.txt'];
    arquivo = fopen ( nomearquivo );

    A = fscanf ( arquivo, '%f', [pop_size generations] );
    fclose( arquivo );
    
    M(i, :) = mean( A );
    f = figure();
    plot(M(i, :), '-');
    hold on
    boxplot(A);
    xlabel('Geracao');
    legend(['valor medio na ' num 'a. execucao']);
    saveas(f, ['grafico' num '.png']);
end

nomearquivo = [funcname '_mean.txt'] ;
arquivo = fopen ( nomearquivo );

A = fscanf ( arquivo, '%f', [generations pop_size] );
A = A';
fclose( arquivo );

P = mean( A );
f = figure();
plot(P, '.');
xlabel('Geracao');
ylabel('Avaliacao');
legend('Media dos individuos');
saveas(f, 'graficoMean.png');

valorMedio = P(pop_size);



nomearquivo = [funcname '_best.txt'];
arquivo = fopen ( nomearquivo );

A = fscanf ( arquivo, '%f', [generations pop_size] );
A = A';
fclose( arquivo );

P = mean( A );
f = figure();
plot(P, '-');
xlabel('Geracao');
ylabel('Avaliacao');
legend('Melhor Individuo');
saveas(f, 'graficoBest.png');

medioMelhores = P(pop_size);


fprintf('================ Resultado ================\n');
fprintf('Funcao: Ackley\n');
fprintf('tamanho da populacao: %d\n', pop_size);
fprintf('Geracoes: %d\n', generations);
fprintf('Valor Médio obtido: %f\n', valorMedio);
fprintf('Valor médio dos melhores indivíduos: %f\n', medioMelhores);
