CREATE PROCEDURE dbo.SP_FINAL_PRICE 
	@complete bit,
	@currentTime Datetime,
    @orderId int,
    @finalPrice DECIMAL(10,3) OUTPUT 
AS
	SET NOCOUNT ON;

    declare @kursor cursor
	declare @idAr int
	declare @cnt int
	declare @discount int
	declare @price int
	declare @temp int

	declare @dodatniPopust bit

	select @dodatniPopust = 0
	if(@complete = 1)
	begin
		if(exists(select * from [Transaction] where IdOrder = @orderId and Amount>=10000 and TimeOfExecution >= DATEADD(DAY,-30,@currentTime) and IdShopBuyer in (select Id from Buyer)))
		begin
			select @dodatniPopust = 1
		end

		update [Order] set DodatniPopust=@dodatniPopust where Id = @orderId
	end
	else
	begin
		select @dodatniPopust = DodatniPopust from [Order] where Id = @orderId
	end
		
	select @finalPrice = 0

	set @kursor = cursor for
	select IdArticle,Count
	from OrderItems
	where IdOrder = @orderId

	open @kursor

	fetch next from @kursor
	into @idAr,@cnt

	while @@FETCH_STATUS = 0
	begin
		select @discount = s.DiscountPercentage, @price = a.ArticlePrice
		from Article a join Shop s on (a.IdShop=s.Id)
		where a.Id = @idAr

		select @temp = (@cnt * @price)*((100-@discount)/100.0)

		if(@dodatniPopust = 1)
		begin
			select @temp = @temp * 0.98
		end

		select @finalPrice = @finalPrice + @temp

		fetch next from @kursor
		into @idAr,@cnt
	end

	close @kursor
	deallocate @kursor


RETURN 0 