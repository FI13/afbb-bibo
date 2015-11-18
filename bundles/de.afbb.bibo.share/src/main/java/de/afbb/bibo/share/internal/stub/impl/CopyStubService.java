package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

public class CopyStubService implements ICopyService {

	private final HashMap<String, Copy> copies = new HashMap<>();

	public CopyStubService() {
		Borrower b = (Borrower) new BorrowerStubService().borrowers.toArray()[0];
		Curator c = new CuratorStubService().curator;
		MediumType t = (MediumType) new TypStubService().types.toArray()[0];
		Copy c1 = new Copy(1, "1.Auflage", "1", new Date(), "Kaffeefleck", new Date(), new Date(), -1, b, b, c, c, 1,
				"9780553582024", "Game of Thrones", "George R. R. Martin", "en", t, "Bantam Books");
		copies.put("1", c1);
	}

	@Override
	public void update(Copy copy) throws ConnectException {
	}

	@Override
	public Copy get(String barcode) throws ConnectException {
		return copies.get(barcode);
	}

	@Override
	public Collection<Copy> getGroupedCopies(Integer id) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCopies(Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub
		throw new ConnectException();

	}

	@Override
	public void returnCopies(Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listCopies(Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void lendCopies(Collection<Copy> copies, boolean printList) throws ConnectException {
		// TODO Auto-generated method stub
	}

}
