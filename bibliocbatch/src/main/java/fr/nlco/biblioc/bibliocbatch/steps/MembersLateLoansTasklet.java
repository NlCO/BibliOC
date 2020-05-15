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

    private final ReminderService _ReminderService;

    @Autowired
    public MembersLateLoansTasklet(ReminderService reminderService) {
        this._ReminderService = reminderService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        _ReminderService.sendReminderMails();
        return RepeatStatus.FINISHED;
    }
}
