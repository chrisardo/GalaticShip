package co.lozano.galacticship;

public class ResultadosVo {
    private int id;
    private String nombre;
    private String genero;
    private int avatar;
    private int puntos;
    //int desbloqueo;
    //private String nivel;
    //private String modo;

    public ResultadosVo() {
    }

    public ResultadosVo(int id, String nombre, String genero, int avatar, int puntos/*, int desbloqueo, String modo*/) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.avatar = avatar;
        this.puntos = puntos;
        //this.desbloqueo= desbloqueo;
        //this.modo = modo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    /*public int getDesbloqueo() {
        return desbloqueo;
    }

    public void setDesbloqueo(int desbloqueo) {
        this.desbloqueo = desbloqueo;
    }*/
    /*public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }*/
}
