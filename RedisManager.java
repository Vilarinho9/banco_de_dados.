package cadastrodealuno;

import redis.clients.jedis.UnifiedJedis;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class RedisManager {
    private UnifiedJedis jedis;

    // Construtor: conecta ao Redis
    public RedisManager() {
        try {
            // 👉 Troque o endpoint e senha pelos seus dados (ou use localhost)
            String endpoint = "redis://localhost:6379";
            this.jedis = new UnifiedJedis(new URI(endpoint));
            System.out.println("✅ Conectado ao Redis!");
        } catch (URISyntaxException e) {
            System.out.println("❌ Erro na URI do Redis: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro ao conectar ao Redis: " + e.getMessage());
        }
    }

    // CREATE
    public void cadastrarAluno(String nome, int idade, String curso, String matricula) {
        String chave = "aluno:" + matricula;

        if (jedis.exists(chave)) {
            System.out.println("⚠️ Já existe um aluno com essa matrícula!");
            return;
        }

        Map<String, String> aluno = new HashMap<>();
        aluno.put("Nome", nome);
        aluno.put("Idade", String.valueOf(idade));
        aluno.put("Curso", curso);
        aluno.put("Matricula", matricula);

        jedis.hset(chave, aluno);
        jedis.sadd("alunos", chave);

        System.out.println("✅ Aluno cadastrado com sucesso!");
    }

    // READ - listar todos
    public void listarAlunos() {
        Set<String> chaves = jedis.smembers("alunos");

        if (chaves.isEmpty()) {
            System.out.println("⚠️ Nenhum aluno cadastrado!");
            return;
        }

        System.out.println("\n=== Lista de Alunos ===");
        for (String chave : chaves) {
            exibirAluno(jedis.hgetAll(chave));
        }
    }

    // READ - buscar por nome
    public void buscarPorNome(String nome) {
        Set<String> chaves = jedis.smembers("alunos");
        boolean encontrado = false;

        for (String chave : chaves) {
            Map<String, String> aluno = jedis.hgetAll(chave);
            if (aluno.get("Nome").toLowerCase().contains(nome.toLowerCase())) {
                exibirAluno(aluno);
                encontrado = true;
            }
        }

        if (!encontrado)
            System.out.println("Nenhum aluno encontrado com esse nome.");
    }

    // UPDATE
    public void atualizarAluno(String matricula, String novoNome, String novaIdade, String novoCurso) {
        String chave = "aluno:" + matricula;

        if (!jedis.exists(chave)) {
            System.out.println("⚠️ Aluno não encontrado!");
            return;
        }

        Map<String, String> aluno = jedis.hgetAll(chave);

        if (!novoNome.isEmpty()) aluno.put("Nome", novoNome);
        if (!novaIdade.isEmpty()) aluno.put("Idade", novaIdade);
        if (!novoCurso.isEmpty()) aluno.put("Curso", novoCurso);

        jedis.hset(chave, aluno);
        System.out.println("✅ Aluno atualizado com sucesso!");
    }

    // DELETE
    public void removerAluno(String matricula) {
        String chave = "aluno:" + matricula;
        if (jedis.del(chave) > 0) {
            jedis.srem("alunos", chave);
            System.out.println("🗑️ Aluno removido com sucesso!");
        } else {
            System.out.println("⚠️ Nenhum aluno encontrado com essa matrícula!");
        }
    }

    // FILTER
    public void listarPorCurso(String curso) {
        Set<String> chaves = jedis.smembers("alunos");
        boolean encontrado = false;

        for (String chave : chaves) {
            Map<String, String> aluno = jedis.hgetAll(chave);
            if (aluno.get("Curso").equalsIgnoreCase(curso)) {
                exibirAluno(aluno);
                encontrado = true;
            }
        }

        if (!encontrado)
            System.out.println("Nenhum aluno encontrado no curso: " + curso);
    }

    // COUNT
    public void exibirTotal() {
        long total = jedis.scard("alunos");
        System.out.println("\n📊 Total de alunos cadastrados: " + total);
    }

    // Mostrar detalhes
    private void exibirAluno(Map<String, String> aluno) {
        System.out.println("-----------------------------");
        System.out.println("👨‍🎓 Nome: " + aluno.get("Nome"));
        System.out.println("🎂 Idade: " + aluno.get("Idade"));
        System.out.println("📘 Curso: " + aluno.get("Curso"));
        System.out.println("🆔 Matrícula: " + aluno.get("Matricula"));
    }

    // Fecha conexão
    public void fechar() {
        if (jedis != null) {
            jedis.close();
            System.out.println("🔌 Conexão com Redis encerrada.");
        }
    }
}