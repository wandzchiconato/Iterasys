// 1 - Pacote

// 2 - Bibliotecas
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class teste {
    // 3.1 - Atributos
    String url; // Endereço do SUT - Software Under Test - Software Sob Teste - Software Alvo
    WebDriver driver; // Objeto Selenium WebDriver
    static String caminhoPrint = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime());

    // 3.2 - Métodos e Funções

    /*
    TestNG oferece diversos momento de execução
    São chamadas de Anotações (Annotations) - precidas por @
    @BeforeSuite - Antes das Suites de Teste - Primeiro de Tudo
        @BeforeTest - Teste - testng.xml
            @BeforeClass - Classe
                @BeforeMethod - Método
                    @Test
                @AfterMethod
            @AfterClass
        @AfterTest
    @AfterSuite - Depois das Suites de Teste - Último de Todos

     */

    // Métodos e Funções de Apoio
    // Método de Print

    public void print(String nomePrint) throws IOException {

        File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(foto, new File("out/prints/" + caminhoPrint + "/" + nomePrint + ".png"));

    }

    // Estrutura para leitura de dados de uma massa em formato CSV

    // Função de leitura do CSV
    private Iterator<Object[]> lerCSV(String nomeMassa) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(nomeMassa));
        ArrayList<Object[]> dados = new ArrayList<Object[]>();
        String linha;
        while ((linha = leitor.readLine()) != null){
            String[] campos = linha.split(";");
            dados.add(campos);
        }
        leitor.close();
        return dados.iterator();
    }

    // Alimentar o DataProvider do Testng com os dados do CSV

    @DataProvider(name = "massaTeste")
    public Iterator<Object[]> massaTeste() throws IOException {
        return lerCSV("db/massaCurso.csv");
    }

    @BeforeMethod() // Executa antes de qualquer @Test dessa classe
    public void iniciar() throws IOException {
        url = "https://www.iterasys.com.br";
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\slovy\\Documents\\FTS127\\drivers\\chrome\\83\\chromedriver.exe");


    }
    @Test(priority = 1, dataProvider = "massaTeste")
    public void consultarCurso(
            String id,
            String termo,
            String titulo,
            String preco,
            String browser) throws IOException, InterruptedException {

        // iniciar o browser
        switch (browser){
            case "chrome":
                driver = new ChromeDriver(); // Instancia o Selenium como ChromeDriver
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "ie":
                driver = new InternetExplorerDriver();
                break;
        }



        // Configuração geral do browser
        driver.manage().timeouts().implicitlyWait(60000, TimeUnit.MILLISECONDS);
        driver.manage().window().maximize();



        driver.get(url); // Abre o navegador no endereço que consta na url
        //print("Consultar Curso - Teste " + id +" - Passo 1 - Abriu o site");
        print(id + "\\Consultar Curso - Passo 1 - Abriu o site");
        System.out.println("Abriu o browser");

        // Escrever o nome do curso na caixa de consulta
        //driver.findElement(By.id("searchtext")).sendKeys("Mantis" + Keys.ENTER); // cola o texto "Mantis"
        driver.findElement(By.id("searchtext")).click(); // Opcional - clicar antes de escrever
        driver.findElement(By.id("searchtext")).clear(); // Opcional - limpar antes de escrever
        driver.findElement(By.id("searchtext")).sendKeys(Keys.chord(termo)); // digita letra por letra
        Thread.sleep(3000);
        //print("Consultar Curso - Teste " + id + " - Passo 2 - Consultou pelo curso Mantis");
        print(id + "\\Consultar Curso - Passo 2 - Consultou pelo curso Mantis");
        driver.findElement(By.id("btn_form_search")).click(); // clica no botão com o icone da Lupa

        // Clicar no botão comprar do curso desejado

        driver.findElement(By.cssSelector("span.comprar")).click();
        //print("Consultar Curso - Teste " + id + " - Passo 3 - Visualiza o curso Mantis entre os resultados");
        print(id + "\\Consultar Curso - Passo 3 - Visualiza o curso Mantis entre os resultados");
        // Validar o titulo e o preço do curso

        // Forma didatica
        // Resultado esperado - O que devia acontecer
        String tituloEsperado = titulo;

        // Resultado Obtido - O que aconteceu
        String tituloObtido = driver.findElement(By.cssSelector("span.item-title")).getText();

        // Validar se o resultado esperado = resultado obtido
        Assert.assertEquals(tituloEsperado, tituloObtido);

        // Forma direta
        Assert.assertEquals(titulo,driver.findElement(By.cssSelector("span.item-title")).getText());
        Assert.assertEquals(preco, driver.findElement(By.cssSelector("span.new-price")).getText());
        //print("Consultar Curso - Teste " + id + " - Passo 4 - Exibiu a página de compra do curso Mantis");
        print(id + "\\Consultar Curso - Passo 4 - Exibiu a página de compra do curso Mantis");
        System.out.println("Localizar o curso");

    }
    @AfterMethod // Executa depois de qualquer @Test dessa classe
    public void finalizar(){
        driver.quit();
        System.out.println("Fechar o browser");
    }
}

// A partir de segunda
// 18h30: Round 1 - Começa a aula - Geral
// 20h00: Parada Técnica - Suporte Round 1
// 20h30: Round 2 - Mais Conteúdo
// 22h00: Parada Técnica - Suporte Round 2

// Nesta tarde de Sábado
// 1 - Tirar prints de tela e salvar no disco
// 2 - Criar um sistema de organização automatica de prints
// Pasta 2020-07-11 15-24
//     - Consultar Curso - Teste 1 - Passo 1 - Abriu o site.png
//     - Consultar Curso - Teste 1 - Passo 2 - Consultou pelo curso Mantis.png
//     - ...
// 3 - Ler uma massa de teste no formato CSV
// id; termo; curso; preco; browser
//  1; Mantis; Mantis; R$ 49,99; chrome
//  2; mantis; Mantis; R$ 49,99; firefox
//  3; TESTLINK; TestLink; R$ 79,99, ie























