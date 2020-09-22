#### Compilção

1. Plugin TornadoFX: Vá para **File** -> **Settings** 
-> **Plugins** e selecione o plugin **TornadoFX**.
2. Selecione o projeto principal (*KtGenetic*) e vá para 
**File** -> **Project Structure**.
3. Em **Project Structure** -> **Project**:
    1. Selecione como 
*Project SDK* o SDK correspondente à versão indicada na 
variável no `jdk.version` no `pom.xml`. Então se estiver 
sendo indicada a versão 1.8, você deve selecionar um SDK 
da versão 1.8
    2. Em *Project Language Level* selecione a versão 
correspondente ao SDK selecionado.
4. Em **Project Structure** -> **Modules**:
    1. Clique com o botão direito sobre o projeto 
*ktfractal* e selecione a opção **Add** -> **TornadoFX**. 
Isso fará com que todos os projetos filhos posam rodar as 
aplicações TornadoFX (ver abaixo).
    2. Verifique se o *Language Level* corresponde ao SDK 
selecionado.

#### Execução
1. Abra um projeto e vá para `src` -> `main` -> `kotlin` 
-> `[pacote]` e abre o arquivo com final `View`.
2. Ao abrir, verifique se ao lado da classe `View` aparece 
o ícone do TornadoFX. 
3. Sobre esse ícone, clique com o botão direto do mouse 
e seleciona a opção **Create ???ViewApp ...**
4. Ao abrir o diálogo de configuração vá para a aba 
**Configuration**, opção **Use classpath of module**. 
***Verifique que o projeto indicado é o mesmo projeto do 
view que você vai rodar!!***. Se não configurar dessa 
forma vai aparecer a mensagem de erro de que não está 
achando a Classe da view.

#### Relação de projetos e de suas Views
| Projeto                                   | Pacote                                                | Classe                        | Observação    |
| -------------                             |:-------------                                         |:-----                         | -----         |
| ktgenetic-balancedtable                   | `rafael.ktgenetic.balancedtable.fx`                   | `BalanceViewApp`              |               |
| ktgenetic-core                            |                                                       |                               | Projeto pai   |
| ktgenetic-equalstring                     | `rafael.ktgenetic.equalstring.fx`                     | `EqualStringsViewApp`         |               |
| ktgenetic-nqueens                         | `rafael.ktgenetic.nqueens.fx`                         | `NBishopsApp`                 |               |
| ktgenetic-nqueens                         | `rafael.ktgenetic.nqueens.fx`                         | `NQueensApp`                  |               |
| ktgenetic-pallete                         | `rafael.ktgenetic.pallete.fx`                         | `PalleteViewApp`              |               |
| ktgenetic-pictures_comparsion             | `rafael.ktgenetic.pictures_comparsion.fx`             | `PicturesComparsionViewApp`   |               |
| ktgenetic-pictures_comparsion-rectangles  | `rafael.ktgenetic.pictures_comparsion.rectangles.fx`  | `PicturesComparsionViewApp`   |               |
| ktgenetic-salesman                        | `rafael.ktgenetic.salesman.fx`                        | `SalesmanViewApp`             |               |
| ktgenetic-sudoku                          |                                                       |                               | Não Funcional |
