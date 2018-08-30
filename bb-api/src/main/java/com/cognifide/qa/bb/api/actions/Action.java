package com.cognifide.qa.bb.api.actions;

import com.cognifide.qa.bb.api.actors.Actor;

public interface Action {

  <T extends Actor> void performAs(T actor);
}
