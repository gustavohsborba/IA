clear all

funcname = 'ACKLEY';
pop_size = 50;
generations = 100;


% for i = 1:3:30
%     num = int2str(i);
%     nomearquivo = [ funcname '_run_' num '.txt'];
%     arquivo = fopen ( nomearquivo );
% 
%     A = fscanf ( arquivo, '%f', [pop_size generations] );
%     fclose( arquivo );
%     
%     M(i, :) = mean( A );
%     f = figure();
%     plot(M(i, :), '-');
%     saveas(f, ['grafico' num '.png']);
%     hold on
%     boxplot(A);
% 
% end

nomearquivo = [funcname '_mean.txt'] ;
arquivo = fopen ( nomearquivo );

A = fscanf ( arquivo, '%f', [pop_size generations] );
A = A';
fclose( arquivo );

P = mean( A );
f = figure();
plot(P, '.');
xlabel('Geracao');
ylabel('Avaliacao');
legend('Media');
saveas(f, 'graficoMean.png');




nomearquivo = [funcname '_best.txt'];
arquivo = fopen ( nomearquivo );

A = fscanf ( arquivo, '%f', [pop_size generations] );
A = A';
fclose( arquivo );

P = mean( A );
f = figure();
plot(P, '-');
xlabel('Geracao');
ylabel('Avaliacao');
legend('Melhor Individuo');
saveas(f, 'graficoBest.png');