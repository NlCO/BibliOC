package fr.nlco.biblioc.bibliocbatch.configuration;

import fr.nlco.biblioc.bibliocbatch.steps.MembersLateLoansTasklet;
import fr.nlco.biblioc.bibliocbatch.steps.RefreshBooksRequestsTasklet;
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

    public final MembersLateLoansTasklet task1;

    public final RefreshBooksRequestsTasklet task2;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                       MembersLateLoansTasklet membersLateLoansTasklet,
                       RefreshBooksRequestsTasklet refreshBooksRequestTask) {
        this.jobs = jobBuilderFactory;
        this.steps = stepBuilderFactory;
        this.task1 = membersLateLoansTasklet;
        this.task2 = refreshBooksRequestTask;
    }

    /**
     * Déclaration du job
     *
     * @return un job
     */
    @Bean
    public Job dailyJob() {
        return jobs.get("dailyJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .start(stepTwo())
                .build();
    }

    /**
     * Déclaration d'une step
     *
     * @return une step
     */
    @Bean
    public Step stepOne() {
        return steps.get("stepOne").tasklet(task1).build();
    }

    /**
     * Déclaration d'une step
     *
     * @return une step
     */
    @Bean
    public Step stepTwo() {
        return steps.get("stepTwo").tasklet(task2).build();
    }

}
