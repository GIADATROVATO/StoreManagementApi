package dev.store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.store.dto.ErpHandler;
import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;

@Service
public class Dispatcher {
	Map<EntityType,ErpHandler> handlers;
	public Dispatcher(List<ErpHandler> list) {
		this.handlers=list.stream().collect(Collectors.toMap(ErpHandler::getType,p->p ));
	/*
	 * arrivano OrdineHandler e ClienteHandler, io faccio ORDINE -> OrdineHandler, CLIENTE -> ClienteHandler	
	 */
	}
	/*
	 *  prendo il tipo, trovo l'handler e controllo se è null 
	 */
	public void dispatch(IntegrationEvent e) {
		ErpHandler erp=	handlers.get(e.getEntityType());
		if(erp ==null) {
			throw new RuntimeException("Handler non trovato per "+e.getEntityType());
		}
		erp.process(e);
	}
}