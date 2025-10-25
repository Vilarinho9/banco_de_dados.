package cadastrodealuno;

import java.util.Scanner;

public class GerenciadorAlunosRedis {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        RedisManager redis = new RedisManager();
        int opcao;

        do {
            System.out.println("\n=== MENU GERENCIADOR DE ALUNOS (REDIS) ===");
            System.out.println("1 - Cadastrar Aluno");
            System.out.println("2 - Listar Todos os Alunos");
            System.out.println("3 - Buscar Aluno por Nome");
            System.out.println("4 - Atualizar Aluno");
            System.out.println("5 - Remover Aluno");
            System.out.println("6 - Listar por Curso");
            System.out.println("7 - Exibir Total de Alunos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrar(redis);
                case 2 -> redis.listarAlunos();
                case 3 -> buscar(redis);
                case 4 -> atualizar(redis);
                case 5 -> remover(redis);
                case 6 -> listarPorCurso(redis);
                case 7 -> redis.exibirTotal();
                case 0 -> System.out.println("👋 Saindo do sistema...");
                default -> System.out.println("⚠️ Opção inválida!");
            }

        } while (opcao != 0);

        redis.fechar();
    }

    private static void cadastrar(RedisManager redis) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());

        System.out.print("Curso: ");
        String curso = scanner.nextLine();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        redis.cadastrarAluno(nome, idade, curso, matricula);
    }

    private static void buscar(RedisManager redis) {
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        redis.buscarPorNome(nome);
    }

    private static void atualizar(RedisManager redis) {
        System.out.print("Digite a matrícula do aluno: ");
        String matricula = scanner.nextLine();

        System.out.print("Novo nome (Enter para manter): ");
        String novoNome = scanner.nextLine();

        System.out.print("Nova idade (Enter para manter): ");
        String novaIdade = scanner.nextLine();

        System.out.print("Novo curso (Enter para manter): ");
        String novoCurso = scanner.nextLine();

        redis.atualizarAluno(matricula, novoNome, novaIdade, novoCurso);
    }

    private static void remover(RedisManager redis) {
        System.out.print("Digite a matrícula do aluno que deseja remover: ");
        String matricula = scanner.nextLine();
        redis.removerAluno(matricula);
    }

    private static void listarPorCurso(RedisManager redis) {
        System.out.print("Digite o curso: ");
        String curso = scanner.nextLine();
        redis.listarPorCurso(curso);
    }
}
