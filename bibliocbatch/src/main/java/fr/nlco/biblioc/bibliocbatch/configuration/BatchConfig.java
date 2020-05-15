package fr.nlco.biblioc.bibliocbatch.configuration;

import fr.nlco.biblioc.bibliocbatch.steps.MembersLateLoansTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de la partie Batch
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public final JobBuilderFactory jobs;

    public final StepBuilderFactory steps;

    public final MembersLateLoansTasklet task;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MembersLateLoansTasklet membersLateLoansTasklet) {
        this.jobs = jobBuilderFactory;
        this.steps = stepBuilderFactory;
        this.task = membersLateLoansTasklet;
    }

    /**
     * Déclaration du job
     *
     * @return un job
     */
    @Bean
    public Job sendReminderJob() {
        return jobs.get("sendReminderJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne()).build();
    }

    /**
     * Déclaration d'une step
     *
     * @return une step
     */
    @Bean
    public Step stepOne() {
        return steps.get("stepOne").tasklet(task).build();
    }


}
