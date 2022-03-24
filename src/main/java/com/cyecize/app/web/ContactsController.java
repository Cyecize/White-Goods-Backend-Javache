package com.cyecize.app.web;

import com.cyecize.app.api.contacts.ContactsDto;
import com.cyecize.app.api.contacts.ContactsService;
import com.cyecize.app.api.question.QuestionQuery;
import com.cyecize.app.api.question.QuestionService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ContactsController {

    private final QuestionService questionService;
    private final ContactsService contactsService;

    @GetMapping(Endpoints.CONTACT_INFO)
    public ContactsDto getContactInfo() {
        return this.contactsService.getContacts();
    }

    @PostMapping(Endpoints.QUESTION)
    public JsonResponse sendQuestion(@Valid QuestionQuery query) {
        this.questionService.sendQuestion(query);
        return new JsonResponse(HttpStatus.OK).addAttribute("message", "Question was sent!");
    }
}
