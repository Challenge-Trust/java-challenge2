package br.com.fiap.soulup.model;

/**
 * Usuário da plataforma SoulUp - tabela T_SU_USUARIO.
 * O campo password guarda o hash BCrypt da senha (coluna senha).
 */
public class Usuario {

    private Long id;
    private String name;
    private String userName;
    private String password;
    private String cpf;
    private int viewMinutes;

    public Usuario() {
    }

    public Usuario(String name, String userName, String password, String cpf, int viewMinutes) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.cpf = cpf;
        this.viewMinutes = viewMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getViewMinutes() {
        return viewMinutes;
    }

    public void setViewMinutes(int viewMinutes) {
        this.viewMinutes = viewMinutes;
    }
}
