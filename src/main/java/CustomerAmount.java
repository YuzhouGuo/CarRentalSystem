/**

SELECT 
	SUM(mship.price_per_month) AS spent
FROM 
	membership mship
	JOIN memberPayment mp 
	ON mp.type = mship.type
	AND mp.transaction_num IN (
		SELECT 
			p.transaction_num
		FROM payment p
		WHERE paid_by = ?
		AND date >= (NOW() - interval '1 year')
	)
;

 */
public class CustomerAmount extends Operation {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
