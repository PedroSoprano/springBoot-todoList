package br.com.pedro.todolist.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedro.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        // esse (UUID) me parece gambiarra TODO: PESQUISAR
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        var taskStartDate = taskModel.getStartAt();
        var taskEndDate = taskModel.getEndAt();

        if (currentDate.isAfter(taskStartDate) || currentDate.isAfter(taskEndDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início / data de término deve ser maior que a data atual.");
        }

        if (taskStartDate.isAfter(taskEndDate)) {
            return ResponseEntity.status(400).body("A data de término deve ser maior que a data de inicio.");
        }

        var task = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("")
    public List<TaskModel> getAllTasks(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        // POR ALGUM MOTIVO SE EU ATUALIZAR A TASK E NÃO PASSA idUser ELE SOME COM A
        // TASK, TODO: pesquisar o pq
        // var idUser = request.getAttribute("idUser");
        // taskModel.setIdUser((UUID) idUser);

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(404).body("Tarefa não encontrada.");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(401).body("Usuário não tem permissão para alterar essa tarefa.");
        }

        Utils.copyNonNullProperties(taskModel, task);

        // taskModel.setId(id);

        var taskAtt = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskAtt);
        // ps: a maioria do código está comentado pois a função que criamos no utils
        // resolve a maior parte dos problemas
        // TODO: estudar mais essas funções no Utils
        // Foi feito tudo isso pois o update estava tendo um comportamento meio chato,
        // quando passávamos apenas uma propriedade para atualizar, ele deixava todo o
        // resto como nulo
    }
}
