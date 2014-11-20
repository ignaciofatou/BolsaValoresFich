package bolsavalores;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.Comparator;
import java.net.URL;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Day;

public class DHistoricos
{
    //Declaracion de Variables
    public int Rango;
    public DHistorico[] Matrix;
    public String NombreValor;
    public int NumeroValores;
    private String FicheroHistoricos;

    //Constructor
    public DHistoricos(int PE_Rango)
    {
        Rango  = PE_Rango;
        Matrix = new DHistorico[PE_Rango];
        NumeroValores = 0;
    }

    //Recupera los Datos del Memoria del Programa y los Guarda en una Matriz de Datos
    public boolean IncluirHistoricosMemoria(String PE_DirectorioIN, String PE_NombreValor, int PE_Redondeo)
    {
        //Declaramos Variables
        FicheroHistoricos = PE_DirectorioIN + PE_NombreValor;        
        FileReader r = null;
        BufferedReader buffer;
        String AuxLinea;
        boolean Resultado = false;

        try
        {
            System.out.println("Tratamos de Abrir el Fichero "+FicheroHistoricos);
            //Iniciamos Variables
            NombreValor = PE_NombreValor;
            NumeroValores = 0;
            r = new FileReader(FicheroHistoricos);
            buffer = new BufferedReader(r);

            //Despreciamos la Primera Linea de Cabecera
            buffer.readLine();

            //Recorremos el Fichero hasta el Final o hasta el Tope de Rango
            while (((AuxLinea = buffer.readLine()) != null) && (NumeroValores < Rango))
            {
                //Reservamos memoria para los Campos Correspondientes
                Matrix[NumeroValores] = new DHistorico();
                
                //Dividimos la Linea en los Campos Correspondientes
                Matrix[NumeroValores].ObtieneValores(NombreValor, AuxLinea, PE_Redondeo);

                //Incrementamos el Contador de Linea
                NumeroValores ++;
            }
            System.out.println("Fin de Fichero Historico de "+PE_NombreValor+" - Se han Leido "+NumeroValores+" Registros");

            //Ordenamos los Datos por Fecha
            OrdenarPorFecha();
            
            //Si todo ha ido Correctamente
            Resultado = true;
        }        
        catch (Exception e){System.out.println("Error1: " + e.getMessage());}
        finally
        {
            try
            {
                if (r != null)
                    r.close();
                
                return Resultado;
            }
            catch (Exception e2)
            {
                System.out.println("Error2: " + e2.getMessage());
                return Resultado;
            }
        }
    }

    //Recupera los Datos del Fichero Local Indicado y los Guarda en una Matriz de Datos
    public boolean IncluirHistoricosLocal(String PE_NombreFichero, String PE_NombreValor, int PE_Redondeo)
    {
        //Declaramos Variables      
        FileReader r = null;
        BufferedReader buffer;
        String AuxLinea;
        boolean Resultado = false;
        DHistorico AuxHistorico;
        int Insertados = 0;

        try
        {
            System.out.println("Tratamos de Abrir el Fichero "+PE_NombreFichero);
            //Iniciamos Variables
            NombreValor = PE_NombreValor;
            r = new FileReader(PE_NombreFichero);
            buffer = new BufferedReader(r);            

            //Despreciamos la Primera Linea de Cabecera
            buffer.readLine();

            //Recorremos el Fichero hasta el Final o hasta el Tope de Rango
            while (((AuxLinea = buffer.readLine()) != null) && (NumeroValores < Rango))
            {
                //Reservamos memoria para los Campos Correspondientes                
                AuxHistorico = new DHistorico();
                
                //Dividimos la Linea en los Campos Correspondientes
                AuxHistorico.ObtieneValores(NombreValor, AuxLinea, PE_Redondeo);

                //Comprobamos si esa Fecha ya Existe o No
                if (BuscarFecha(AuxHistorico.Fecha) == -1)
                {
                    //Reservamos memoria para los Campos Correspondientes
                    Matrix[NumeroValores] = new DHistorico();
                    Matrix[NumeroValores] = AuxHistorico;

                    //Incrementamos el Contador de Datos Historicos Almacenados
                    NumeroValores ++;
                    Insertados ++;
                }
            }
            System.out.println("Fin de Fichero Historico de "+PE_NombreValor+" - Se han Leido "+NumeroValores+" Registros y Se han Guardado "+Insertados+" Registros");

            //Ordenamos los Datos por Fecha
            OrdenarPorFecha();
            
            //Guardamos los Datos de Memoria
            ActualizarFichValores();
            
            //Si todo ha ido Correctamente
            Resultado = true;
        }
        catch (Exception e){System.out.println("Error1: " + e.getMessage());}
        finally
        {
            try
            {
                if (r != null)
                    r.close();
                
                return Resultado;
            }
            catch (Exception e2)
            {
                System.out.println("Error2: " + e2.getMessage());
                return Resultado;
            }
        }
    }
    
    //Recupera los Datos de la URL Indicada y los Guarda en una Matriz de Datos
    public boolean IncluirHistoricosURL(String PE_DireccionURL, String PE_NombreValor, int PE_Redondeo)
    {
        URL AuxURL;
        DHistorico AuxHistorico;
        int Insertados = 0;
        
        try 
        {
            //Establecemos la URL desde la que vamos a Recuperar los Datos
            AuxURL = new URL(PE_DireccionURL);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(AuxURL.openStream()));
            String AuxLinea;

            //Despreciamos la Primera Linea de Cabecera
            buffer.readLine();

            //Recorremos el Fichero hasta el Final o hasta el Tope de Rango
            while (((AuxLinea = buffer.readLine()) != null) && (NumeroValores < Rango))
            {
                //Reservamos memoria para los Campos Correspondientes
                AuxHistorico = new DHistorico();
                
                //Dividimos la Linea en los Campos Correspondientes
                AuxHistorico.ObtieneValores(NombreValor, AuxLinea, PE_Redondeo);

                //Comprobamos si esa Fecha ya Existe o No
                if (BuscarFecha(AuxHistorico.Fecha) == -1)
                {
                    //Reservamos memoria para los Campos Correspondientes
                    Matrix[NumeroValores] = new DHistorico();
                    Matrix[NumeroValores] = AuxHistorico;

                    //Incrementamos el Contador de Datos Historicos Almacenados
                    NumeroValores ++;
                    Insertados ++;
                }
            }
            buffer.close();

            System.out.println("Fin de Fichero Historico de "+PE_NombreValor+" - Se han Leido "+NumeroValores+" Registros y Se han Guardado "+Insertados+" Registros");

            //Ordenamos los Datos por Fecha
            OrdenarPorFecha();
            
            //Guardamos los Datos de Memoria
            ActualizarFichValores();
            
            //Si todo ha ido Correctamente
            return true;
        }
        catch(MalformedURLException e1)
        {
            System.out.println("Error al Acceder a la Web: " + e1.getMessage());
            return false;
        }
        catch (Exception e2)
        {
            System.out.println("Error1: " + e2.getMessage());
            return false;
        }
    }
    
    //Guarda los Datos Historicos de Memoria en Fichero
    private void ActualizarFichValores()
    {
        //Declaracion de Variables
        File AuxFichero;
        FileWriter AuxEscritorArchivo;

        try
        {
            AuxFichero             = new File(FicheroHistoricos);
            AuxEscritorArchivo     = new FileWriter(AuxFichero);
            BufferedWriter AuxBfWr = new BufferedWriter(AuxEscritorArchivo);
            PrintWriter AuxSalida  = new PrintWriter(AuxBfWr);
            
            //Incluimos la Cabecera
            AuxSalida.write("Fecha	Cierre	Var	Var (%)	Máx	Mín	\n");

            //Incluimos el Resto de las Lineas
            for (int x = 0; x < NumeroValores; x++)
                AuxSalida.write(Matrix[x].Fecha + '	' + Matrix[x].ValorCierre + '	' + Matrix[x].Variacion + '	' + Matrix[x].PorVariacion + '	' + Matrix[x].Maximo + '	' + Matrix[x].Minimo+"	\n");

            AuxSalida.close();
            AuxBfWr.close();
        }
        catch (Exception e){System.out.println("Error: " + e.getMessage());}
    }
    
    public void CargaGrill(javax.swing.JTable PE_TablaHistorica)
    {
        //Declaracion de Variables
        String Cabecera[]= {"Nombre", "Fecha", "Cierre", "Máximo", "Mínimo", "Variación", "%Var", "S2", "S1", "PivotP", "R1", "R2"};
        String Datos[][] = {};
        DefaultTableModel modelo = new DefaultTableModel(Datos, Cabecera);
        PE_TablaHistorica.setModel(modelo);
        Object ODatos[]= {"", "", "", "", "", "", "", "", "", "", "", ""};
        int Anchos[]={120, 70, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};

        //Establecemos el Tamaño de Cada Columna
        for (int i = 0; i < PE_TablaHistorica.getColumnCount(); i++)
            PE_TablaHistorica.getColumnModel().getColumn(i).setPreferredWidth(Anchos[i]);
       
        try
        {            
            //Recorremos toda la Matriz
            for (int x = 0; x < Rango; x++)
            {
                if (Matrix[x] != null)
                {
                    ODatos[0]  = Matrix[x].NombreValor;
                    ODatos[1]  = Matrix[x].Fecha;
                    ODatos[2]  = Matrix[x].ValorCierre;
                    ODatos[3]  = Matrix[x].Maximo;
                    ODatos[4]  = Matrix[x].Minimo;
                    ODatos[5]  = Matrix[x].Variacion;
                    ODatos[6]  = Matrix[x].PorVariacion;
                    ODatos[7]  = Matrix[x].PivotPoint;
                    ODatos[8]  = Matrix[x].R1;
                    ODatos[9]  = Matrix[x].S1;
                    ODatos[10] = Matrix[x].R2;
                    ODatos[11] = Matrix[x].S2;

                    //Añadimos la Fila Nueva
                    modelo.addRow(ODatos); 
                }
            }
        }
        catch(Exception e){System.out.println("Error al Rellenar la Tabla "+ e.getMessage());}
    }

    //Muestra la Grafica del Valor Actual
    public void MostrarGrafica(javax.swing.JPanel PE_PanelGrafico, javax.swing.JLabel PE_LabelGrafico, 
                               int PE_Rango, boolean PE_IndCierre, boolean PE_IndMaximo, boolean PE_IndMinimo, 
                               boolean PE_IndR2, boolean PE_IndR1, boolean PE_IndPvPo, boolean PE_IndS1, boolean PE_IndS2)
    {
        TimeSeries Maximo       = new TimeSeries("Máximo");
        TimeSeries Cierre       = new TimeSeries("Cierre");        
        TimeSeries Minimo       = new TimeSeries("Mínimo");        
        TimeSeries Resistencia2 = new TimeSeries("R2");
        TimeSeries Resistencia1 = new TimeSeries("R1");
        TimeSeries PivotPoint   = new TimeSeries("PivotP");
        TimeSeries Soporte1     = new TimeSeries("S1");
        TimeSeries Soporte2     = new TimeSeries("S2");

        for (int cont = 0; (cont < PE_Rango) && (cont < NumeroValores); cont++)
        {
            if (PE_IndMaximo)
                Maximo.add      (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].Maximo);
            if (PE_IndCierre)
                Cierre.add      (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].ValorCierre);
            if (PE_IndMinimo)
                Minimo.add      (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].Minimo);
            if (PE_IndR2)
                Resistencia2.add(new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].R2);
            if (PE_IndR1)
                Resistencia1.add(new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].R1);
            if (PE_IndPvPo)
                PivotPoint.add  (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].PivotPoint);
            if (PE_IndS1)
                Soporte1.add    (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].S1);
            if (PE_IndS2)
                Soporte2.add    (new Day(Matrix[cont].FechaDD, Matrix[cont].FechaMM, Matrix[cont].FechaYYYY), Matrix[cont].S2);
        }

        //Añande la Serie para el Conjunto de Datos
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        //Incluimos las Series al Conjunto de Datos
        if (PE_IndMaximo)
            dataset.addSeries(Maximo);
        if (PE_IndCierre)
            dataset.addSeries(Cierre);
        if (PE_IndMinimo)
            dataset.addSeries(Minimo);
        if (PE_IndR2)
            dataset.addSeries(Resistencia2);
        if (PE_IndR1)
            dataset.addSeries(Resistencia1);
        if (PE_IndPvPo)
            dataset.addSeries(PivotPoint);
        if (PE_IndS1)
            dataset.addSeries(Soporte1);
        if (PE_IndS2)
            dataset.addSeries(Soporte2);
        
        //Genera la Grafica
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Datos Historicos", "Fecha", "Precio", dataset, true, true, false);

        //Pasamos la Grafica como Imagen a un Label
        BufferedImage graficoLinea = chart.createBufferedImage(PE_PanelGrafico.getWidth(), PE_PanelGrafico.getHeight());
        PE_LabelGrafico.setSize(PE_PanelGrafico.getSize());
        PE_LabelGrafico.setIcon(new ImageIcon(graficoLinea));
        PE_PanelGrafico.updateUI();
    }

    //Busca una Fecha en la Lista de Datos Historicos 
    //Retorna la Posición ó -1 en Caso de no encontrarla
    public int BuscarFecha(String PE_Fecha)
    {
        //Si hay Datos
        if (NumeroValores != 0)
        {
            //Si PE_Fecha es mayor que la Fecha de la Primera Posicion -> No Existe
            if (PE_Fecha.compareTo(Matrix[0].Fecha) > 0)
                return -1;            
            //Si PE_Fecha es menor que la Fecha de la Ultima Posicion -> No Existe
            else if (PE_Fecha.compareTo(Matrix[NumeroValores - 1].Fecha) < 0)
                return -1;        
            //En Caso contrario Buscamos en Medio
            else
            {
                //Recorremos el Resto de Fechas de los Datos Historicos
                for (int x = 0; x < (NumeroValores); x++)
                    if (PE_Fecha.equals(Matrix[x].Fecha))
                        return x;
            
                //No lo Encuentra (Caso Extraño)
                return -1;
            }            
        }
        //Si no hay Datos
        else
            return -1;
    }

    //Ordena los Datos de la Matriz por Fecha
    public void OrdenarPorFecha()
    {
        Arrays.sort(Matrix, 0, NumeroValores, new ComparaFecha());
    }
    
    //Implementamos el Metodo de Comparacion Necesario
    class ComparaFecha implements Comparator<DHistorico>
    {
        //Hacemos un Order Descencente
        public int compare(DHistorico Campo1, DHistorico Campo2) 
        {
            if (Campo1.Fecha.equals(Campo2.Fecha))
                return 0;
            else if (Campo1.Fecha.compareTo(Campo2.Fecha) > 0)
                return -1;
            else
                return 1;
        }
    }
}