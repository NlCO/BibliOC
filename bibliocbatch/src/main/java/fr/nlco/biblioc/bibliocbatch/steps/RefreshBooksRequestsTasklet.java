package fr.nlco.biblioc.bibliocbatch.steps;

import fr.nlco.biblioc.bibliocbatch.proxies.BibliocapiProxy;
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
public class RefreshBooksRequestsTasklet implements Tasklet {

    private final BibliocapiProxy bibliocapiProxy;

    @Autowired
    public RefreshBooksRequestsTasklet(BibliocapiProxy bibliocapiProxy) {
        this.bibliocapiProxy = bibliocapiProxy;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        bibliocapiProxy.refreshRequests();
        return RepeatStatus.FINISHED;
    }
}
