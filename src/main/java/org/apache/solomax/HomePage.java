package org.apache.solomax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;
import org.wicketstuff.select2.Select2Choice;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private final static int PAGE_SIZE = 20;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		final Select2Choice<Integer> number = new Select2Choice<>("number", Model.of((Integer)null), new ChoiceProvider<Integer>() {
			private static final long serialVersionUID = 1L;


			@Override
			public String getDisplayValue(Integer object) {
				return String.valueOf(object);
			}

			@Override
			public String getIdValue(Integer object) {
				return object == null ? null : String.valueOf(object);
			}

			@Override
			public void query(String term, int page, Response<Integer> response) {
				for (int i = page * PAGE_SIZE; i < (page + 1) * PAGE_SIZE; ++i) {
					if (term == null || String.valueOf(i).contains(term)) {
						response.add(i);
					}
				}
				response.setHasMore(true);
			}

			@Override
			public Collection<Integer> toChoices(Collection<String> ids) {
				List<Integer> numbers = new ArrayList<>();
				for (String id : ids) {
					numbers.add(Integer.valueOf(id));
				}
				return new ArrayList<>(numbers);
			}
		});
		final Label numerLbl = new Label("label", "");
		add(numerLbl.setOutputMarkupId(true));
		number.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(numerLbl.setDefaultModelObject(number.getModelObject()));
			}
		});
		final Form<Void> form = new Form<>("form");
		add(form.add(number, new FileUploadField("file")).setOutputMarkupId(true));
		form.add(new AjaxButton("reset") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				number.setModelObject(null);
				target.add(form,
						numerLbl.setDefaultModelObject(number.getModelObject()));
			}
		});
	}
}
