public class Combo {
    private List<Alimento> alimentosCombo;
    private String nombreCombo;
    private int precioCombo;

    public Combo(String nombreCombo, int precioCombo) {
        this.nombreCombo = nombreCombo;
        this.alimentos = new ArrayList<>();
        this.precioCombo = precioCombo;
    }

    // Getters y Setters
    public String getNombreCombo() {
        return nombreCombo;
    }

    public void setNombreCombo(String nombreCombo) {
        this.nombreCombo = nombreCombo;
    }

    public int getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(int precioCombo) {
        this.precioCombo = precioCombo;
    }

    public List<Alimento> getAlimentosCombo() {
        return alimentosCombo;
    }

    public void agregarAlimento(Alimento alimentoCombo) {
        this.alimentos.add(alimentoCombo);
    }

    // Métodos

    public Combo crearComboAmix() {
        this.nombreCombo = "Combo amix";
        this.precioCombo = 170; // $30 de ahorro
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 0)); // Precio original: 80
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 0));
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 0)); // Precio original: 60
        return this; // Retorna el combo creado
    }

    public Combo crearComboNachos() {
        this.nombreCombo = "Combo Nachos";
        this.precioCombo = 250; // $40 de ahorro
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 0)); 
        this.alimentosCombo.add(new Alimento("Nachos", "Jumbo", 0)); // Precio original: 90
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 0));
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 0));
        return this; // Retorna el combo creado
    }

    public Combo crearComboBuenTrio() {
        this.nombreCombo = "Combo Buen Trio";
        this.precioCombo = 350; // $50 de ahorro
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Mega", 0)); // Precio original: 90
        this.alimentosCombo.add(new Alimento("Nachos", "Mega", 0)); // Precio original: 100
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 0)); // Precio original: 70
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 0));
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 0));
        return this; // Retorna el combo creado
    }

    public Combo crearComboQueMeVes() {
        this.nombreCombo = "Combo Que Me Ves";
        this.precioCombo = 200; // $30 de ahorro
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 0)); 
        this.alimentosCombo.add(new Alimento("Nachos", "Jumbo", 0)); 
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 0));
        return this; // Retorna el combo creado
    }

    public Combo crearOrdenPersonalizada(List<Alimento> alimentosPersonalizados) {
        this.nombreCombo = "Orden Personalizada";
        this.alimentosCombo = alimentosPersonalizados;
        this.precioCombo = 0;
        for (Alimento alimento : alimentosPersonalizados) {
            this.precioCombo += alimento.getPrecio();
        }
        return this; // Retorna el combo creado
    }
}
