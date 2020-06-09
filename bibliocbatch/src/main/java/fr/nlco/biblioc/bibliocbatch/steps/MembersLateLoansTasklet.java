package fr.nlco.biblioc.bibliocbatch.steps;

import fr.nlco.biblioc.bibliocbatch.service.ReminderService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Tasklet décrivant la task à réaliser
 */
@Component
public class MembersLateLoansTasklet implements Tasklet {

    private final ReminderService reminderService;

    @Autowired
    public MembersLateLoansTasklet(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        reminderService.sendReminderMails();
        return RepeatStatus.FINISHED;
    }
}
