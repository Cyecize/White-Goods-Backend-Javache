package com.cyecize.app.api.question;

import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.api.user.UserService;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Override
    @Transactional
    public void sendQuestion(QuestionQuery query) {
        final Question question = this.modelMapper.map(query, Question.class);
        this.questionRepository.saveQuestion(question);

        final QuestionDto questionDto = this.modelMapper.map(question, QuestionDto.class);
        this.mailService.sendEmail(EmailTemplate.QUESTION, questionDto, this.userService.getEmailsOfAdmins());
    }
}
