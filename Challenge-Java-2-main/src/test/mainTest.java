package test;

import dao.UsuarioDAO;
import model.Usuario;

public class mainTest {
    public static void main(String[] args){
        Usuario novoUsuario = new Usuario("Ana Silva", "ana123", "senha123", "12345678900", 200);
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.adicionarUsuario(novoUsuario);
        System.out.println("ID gerado: " + novoUsuario.getId());

        Usuario encontrado = usuarioDAO.findById(novoUsuario.getId());
        System.out.println(encontrado.getName()); // deveria imprimir "Ana Silva"

    }
}
