package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.Game;
import com.example.provaH2.gestioneGioco.GameList;
import com.example.provaH2.guess.GameController;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringView(name = "statistiche")
@com.vaadin.annotations.JavaScript({ "https://www.gstatic.com/charts/loader.js", "BarChart.js" })
public class StatisticheView extends VerticalLayout implements View {

    @Autowired
    private PartitaRepository partitaRepository;
    private Account account;
    private VerticalLayout layoutDestro;
    private Label nomeGioco;
    private Label numPartite;
    private Label dataUltimaPartita;
    private Label puntiUltimaPartita;
    private FormLayout formUltimaPartita;
    private Label noPartite;
    private HorizontalLayout layoutChart;

   // private FormLayout form;
    @Autowired
    private GameList gameList;

     @PostConstruct
    protected  void  initialize(){
         account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
         /*List<Partita>partiteAll=partitaRepository.cercaByGioco(account, "unGioco");
         System.out.println("tutte le mie parite");
         partiteAll.forEach(System.out::println);
*/
         //TODO: i marigni di questo nn sono gli stessi dei settings
         addStyleName("SettingsVL");
         setSpacing(false);
         setMargin(true);
         setWidth(100, Unit.PERCENTAGE);
         setHeight(100,Unit.PERCENTAGE);

         Label title = new Label("Statistiche");
         title.addStyleName(ValoTheme.LABEL_H1);
         addComponent(title);

         HorizontalLayout statisticheLayout=new HorizontalLayout();
         statisticheLayout.addStyleName("statisticheLayout");
         statisticheLayout.setWidth(100,Unit.PERCENTAGE);
         statisticheLayout.setHeight(100,Unit.PERCENTAGE);
         //statisticheLayout.setMargin(true);

         /***************sx***************************/
         Panel panelSinistro= new Panel();
         panelSinistro.setWidth(180, Unit.POINTS);
         panelSinistro.setHeight(100,Unit.PERCENTAGE);

         VerticalLayout layoutNomiGiochi= new VerticalLayout();
         panelSinistro.setContent(layoutNomiGiochi);
         layoutNomiGiochi.setWidth(100, Unit.PERCENTAGE);
         layoutNomiGiochi.setSpacing(false);

         List<String> gameNames= gameList.getGameNames();
         gameNames.forEach(s -> {
             Button nome=new Button(s);
             nome.setWidth(100,Unit.PERCENTAGE);
             nome.addStyleName(ValoTheme.BUTTON_BORDERLESS);
             nome.addStyleName("buttonGameName");
             nome.addClickListener(clickEvent ->{
                 //setChart();
                 onGameClick(nome.getCaption());
             });
             layoutNomiGiochi.addComponent(nome);
         });

         //TODO:togli
         Button nome1=new Button("unGioco");
         nome1.setWidth(100,Unit.PERCENTAGE);
         nome1.addStyleName(ValoTheme.BUTTON_BORDERLESS);
         nome1.addStyleName("buttonGameName");
         nome1.addClickListener(clickEvent -> onGameClick(nome1.getCaption()));
         layoutNomiGiochi.addComponent(nome1);
         for(int i=0; i<20;i++){
             Button nome=new Button("nomi giochi " +i);
             nome.setWidth(100,Unit.PERCENTAGE);
             nome.addStyleName(ValoTheme.BUTTON_BORDERLESS);
             nome.addStyleName("buttonGameName");
             //nome.addStyleName(ValoTheme.BUTTON_SMALL);
             nome.addClickListener(clickEvent -> onGameClick(nome.getCaption()));
             layoutNomiGiochi.addComponent(nome);
         }

         /**************dx****************************/
         Panel panelDestro = new Panel();
         panelDestro.setWidth(100, Unit.PERCENTAGE);
         panelDestro.setHeight(100,Unit.PERCENTAGE);
         panelDestro.addStyleName(ValoTheme.PANEL_BORDERLESS);
         layoutDestro= new VerticalLayout();
         layoutDestro.setWidth(100,Unit.PERCENTAGE);
         //layoutDestro.setHeight(100,Unit.PERCENTAGE);
         layoutDestro.setMargin(false);
         panelDestro.setContent(layoutDestro);

         nomeGioco= new Label("");
         nomeGioco.addStyleName(ValoTheme.LABEL_H2);
         layoutDestro.addComponent(nomeGioco);
         formUltimaPartita= new FormLayout();
         formUltimaPartita.setMargin(false);
         formUltimaPartita.setSpacing(false);
         formUltimaPartita.setWidth(100,Unit.PERCENTAGE);
         numPartite=new Label();
         numPartite.setWidth(100,Unit.PERCENTAGE);
         numPartite.setCaption("partite giocate");
         formUltimaPartita.addComponent(numPartite);
         dataUltimaPartita= new Label();
         dataUltimaPartita.setWidth(100,Unit.PERCENTAGE);
         dataUltimaPartita.setCaption("ultima partita il:");
         formUltimaPartita.addComponent(dataUltimaPartita);
         puntiUltimaPartita= new Label();
         puntiUltimaPartita.setCaption("punti ultima partita: ");
         puntiUltimaPartita.setWidth(100, Unit.PERCENTAGE);
         formUltimaPartita.addComponent(puntiUltimaPartita);
         layoutDestro.addComponent(formUltimaPartita);
         noPartite=new Label();
         noPartite.setWidth(100,Unit.PERCENTAGE);
         noPartite.addStyleName(ValoTheme.LABEL_H3);
         noPartite.setValue("Non hai effettuato partite di questo gioco");
         layoutChart= new HorizontalLayout();
         layoutChart.setId("CHARTID");
         layoutChart.setHeight(250, Unit.POINTS);
         layoutChart.setWidth(100,Unit.PERCENTAGE);

         statisticheLayout.addComponents(panelSinistro,panelDestro);
         //statisticheLayout.setExpandRatio(panelSinistro, 1.3f);
         statisticheLayout.setExpandRatio(panelDestro, 2f);
         addComponent(statisticheLayout);
         setExpandRatio(statisticheLayout,2f);

         onGameClick(gameList.getGameList().get(0).getNomeGioco());
         //List<Partita> partite= partitaRepository.cercaPerAccountEGioco(account, "Guess");
         //partite.forEach(System.out::println);

        // setChart();
         /*
         form = new FormLayout();
         form.setMargin(false);
         form.setWidth("100%");
         addComponent(form);

         if(partite.size()==0){

             Label label1= new Label("Non hai ancora effettuato una partita");
             addComponent(label1);

             Button goHome= new Button("go Home");
             goHome.addClickListener(clickEvent -> {
                 Page.getCurrent().setLocation("/private/home");
             });

             addComponent(goHome);
             setComponentAlignment(label1, Alignment.TOP_CENTER);
             setComponentAlignment(goHome, Alignment.TOP_CENTER);
             return;
         }

         String giocoCorrente="";
         for (Partita p: partite) {
             if(!giocoCorrente.equals(p.getGioco())){
                 giocoCorrente=p.getGioco();
                 Label section = new Label(p.getGioco());
                 section.addStyleName(ValoTheme.LABEL_H3);
                 section.addStyleName(ValoTheme.LABEL_COLORED);
                 form.addComponent(section);
             }
             Label tempo= new Label(p.getTimestamp().toString());
             tempo.setCaption("data");
             form.addComponent(tempo);

             //form.addComponent(new Label("partecipanti:"));
             FormLayout punteggi= new FormLayout();
             punteggi.setCaption("punteggi:");
             for (Punteggio punteggio: p.getArray()){
                 Label giocatore= new Label(punteggio.getPunti()+ "");
                 giocatore.setCaption(punteggio.getAccount().getFullName());
                 punteggi.addComponent(giocatore);
             }
             form.addComponent(punteggi);
         }
        */
         //addComponent(new Label("statistiche"));

    }

    private  void onGameClick(String gioco){
       //layoutDestro.removeAllComponents();
       nomeGioco.setValue(gioco);

        List<Partita> partite= partitaRepository.cercaPerAccountEGioco(account, gioco);
        System.out.println("paritite");
        partite.forEach(System.out::println);
        if(partite==null || partite.size()==0){
            layoutDestro.removeComponent(formUltimaPartita);
            layoutDestro.removeComponent(layoutChart);
            layoutDestro.addComponent(noPartite);
        }else{
            layoutDestro.removeComponent(noPartite);
            layoutDestro.addComponent(formUltimaPartita);
            numPartite.setValue(partite.size()+ "");
            Partita last= partite.get(partite.size()-1);
            List<Punteggio> punteggiLastParita= last.getArray();
            List<Punteggio> punteggi= punteggiLastParita.stream().filter(punteggio -> punteggio.getAccount().getId()==account.getId()).collect(Collectors.toList());

            String lastData=new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(last.getTimestamp());
            dataUltimaPartita.setValue(lastData);
            puntiUltimaPartita.setValue(punteggi.get(0).getPunti() + "");

            LinkedHashMap<String, Integer> punteggiMiei= new LinkedHashMap<>();
            partite.forEach(partita -> {
                for(Punteggio p: partita.getArray()){
                    if(p.getAccount().getId()==account.getId()){
                        String data=new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(partita.getTimestamp());
                        punteggiMiei.put(data, p.getPunti());
                        break;
                    }
                }
            });
             layoutDestro.addComponent(layoutChart);
            setChart(gioco, punteggiMiei);
            System.out.println("\n punteggi miei");
            System.out.println(punteggiMiei);
        }
    }

    private void setChart(String gioco, LinkedHashMap<String, Integer> punteggi){
        punti="";
        punteggi.forEach((s, integer) -> {
            punti+=  "['"+s.substring(0,5)+"',"+integer+"],";
        });
        String js=" google.charts.load('current', {'packages':['corechart']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "          ['Date',  'Points'],\n" +
          /*      "          ['2004',         400],\n" +
                "          ['2004',         400],\n" +
                "          ['2004',         400],\n" +
                "          ['2005',         404],\n" +
                "          ['2005',         460],\n" +
                "          ['2006',        1120],\n" +
                "          ['2007',         540]\n" +
            */
                punti+
                "        ]);\n" +
                "\n" +
                "        var options = {\n" +
                "          title: '"+gioco+"',\n" +
                "          curveType: 'none',\n" +
                "          legend: { position: 'bottom' },\n" +
                "          animation:{duration: 1000, easing: 'out', startup:true },\n"+
                "          pointSize:3,\n"+
                "          backgroundColor:'#fafafa',\n"+
                "          colors:['#007d99' ] "+
                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.LineChart(document.getElementById('CHARTID'));\n" +
                "\n" +
                "        chart.draw(data, options);\n" +
                "      }"+
                "\n" +
                "      function disegna() {\n" +
                "        var chart = new google.visualization.LineChart(document.getElementById('CHARTID'));\n" +
                "        chart.draw(data, options);\n" +
                "      }";

        JavaScript.getCurrent().execute(js);
       // JavaScript.getCurrent().execute("disegna()");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
    String punti="";
}
